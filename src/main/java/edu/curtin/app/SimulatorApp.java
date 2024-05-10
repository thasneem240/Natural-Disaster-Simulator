package edu.curtin.app;
import edu.curtin.responders.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimulatorApp
{
	private static final Logger LOGGER = Logger.getLogger(SimulatorApp.class.getName());
	private static Scanner scanner = null;
	private static List<EmergencyDetail> emergencyDetailList = new ArrayList<>();


	public static void main(String[] args)
	{
		String fileName;
		List<String> pollMessage;
		long time = 0;
		long startTime;
		long endTime;
		String endCondition = "";
		List<Emergency> emergencyList = new ArrayList<>();
		ResponderComm responders;

		if (args.length == 1)
		{
			fileName = args[0];
			try
			{
				readFile(fileName);

				/* Create responder Object inorder to communicate with responders */
				responders = new ResponderCommImpl();
				LOGGER.info("\n responder Object created");

				/* Create List of Emergency Objects by using Factory class */
				for (EmergencyDetail emerDetail: emergencyDetailList)
				{
						EmergencyAndObserverFactory.makeEmergency(emerDetail,emergencyList,responders);
				}

				startTime = (System.currentTimeMillis() /1000L);

				while(true)
				{
					System.out.println("\n=================================================================\n");
					System.out.println("\n\t In " + time + " second\n");
					pollMessage = responders.poll();

					/* Validate the Poll Message*/
					if( pollMessage.size() >= 1)
					{
						/* If it is invalid logged the error */
						if (!validatePollExpression(pollMessage))
						{
							LOGGER.log(Level.SEVERE,"\n Invalid Poll Message!! ");

							/* If the poll message is invalid reset the poll message to empty */
							pollMessage = new ArrayList<>();

							LOGGER.info("\n Poll Message Set to Empty ");
						}
					}

					/* Get the End condition of Simulation */
					if(pollMessage.size() == 1)
					{
						endCondition = pollMessage.get(0);
					}

					if(endCondition.equals("end"))
					{
						LOGGER.info("\n Simulation Ends ");
						break;
					}

					/* Do the Emergency task */
					for (Emergency emergency: emergencyList)
					{
						emergency.doEmergencies(time,pollMessage);
					}


					/* Sleep for 1 second */
					Thread.sleep(1000);

					endTime = (System.currentTimeMillis() /1000L);

					time = endTime-startTime;
					
				}
			}
			catch (MyException | InterruptedException e)
			{
				LOGGER.log(Level.SEVERE, "Error While run the Simulator!!", e);
				System.out.println("\n" + e);
			}

		}
		else
		{
			System.out.println("\n Please input a fileName in commandLine");
			usage();
		}
	}



	/* Usage Message */
	private static void usage()
	{
		System.out.println(" Usage: ./gradlew run --args=fileName");
		System.out.println("\n Example: ./gradlew run --args=data.txt");
		System.out.println(" Important --> No spaces between args,= and filename\n");
	}



	private static void readFile(String fileName) throws MyException
	{
		String regExpression = "[0-9]+ (fire|flood|chemical) .+";
		String str, regStr;
		int line = 0;

		try
		{
			File file = new File(fileName);
			scanner = new Scanner(file);

			boolean hasLine = scanner.hasNext();


			while (hasLine)
			{
				line++;

				str = scanner.nextLine();

				Pattern p = Pattern.compile(regExpression);
				Matcher m = p.matcher(str);

				/*If the line matches the regular expression of given format */
				if(m.matches())
				{
					regStr = m.group();
					String[] splitline = regStr.split(" ");

					int time =Integer.parseInt(splitline[0]);
					String emerName = splitline[1];
					String emerLocation = "";

					/* Get the Location */
					for (int i = 2; i < splitline.length; i++)
					{
						if(i != 2)
						{
							emerLocation = emerLocation + " ";
						}
						emerLocation = emerLocation + splitline[i];
					}

					/* Create EmergencyDetail Object */

					if(checkUniqueness(emerName,emerLocation))
					{
						EmergencyDetail emergencyDetail = new EmergencyDetail(time,emerName,emerLocation);

						emergencyDetailList.add(emergencyDetail);

						String logMessage = String.format("\n Object emergencyDataList created with time = %d, emerName = %s, " +
								"emerLocation = %s ",time,emerName,emerLocation);
						LOGGER.info(logMessage);
					}
					else
					{
						String logMessage = String.format("\n In line no %d : Invalid data!! there can only be " +
								"one %s in %s in a given Simulation!! ",line,emerName,emerLocation);
						LOGGER.log(Level.WARNING,logMessage);
					}

				}
				else
				{
					String logMessage = String.format("\n In line no %d : \"%s\" has Invalid data Format!! ",line,str);
					LOGGER.log(Level.WARNING,logMessage);
				}

				hasLine = scanner.hasNext();
			}

		}
		catch (FileNotFoundException e)
		{
			LOGGER.log(Level.SEVERE, "FileNotFoundException in Method readFile!!", e);
			throw new MyException(" Could not read from " + fileName, e);
		}

		scanner.close();
	}


	/* To Check the Uniqueness of data [ line after the integer should unique ] */
	private static boolean checkUniqueness(String emerName, String emerLocation)
	{
		boolean isUnique = true;

		for (EmergencyDetail e: emergencyDetailList)
		{
			if(emerName.equals(e.getEmerName()) && emerLocation.equals(e.getEmerLocation()))
			{
				isUnique = false;
				break;//End the Loop
			}
		}

		return isUnique;
	}


	/* To validate the Poll Expression */
	private static boolean validatePollExpression(List<String> pollMessage)
	{
		boolean isValid = true;

		String regExpression = "end|(fire|flood|chemical) [+-] .+";

		Pattern p = Pattern.compile(regExpression);

		for (String str: pollMessage)
		{
			Matcher m = p.matcher(str);

			/*If the String not matches the regular expression of given format */
			if(!m.matches())
			{
				isValid = false;
			}
		}

		return isValid;
	}
}
