package com.uat.suite.tm_attachments;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.uat.base.Credentials;
import com.uat.util.TestUtil;

@Listeners(com.uat.listener.TestsListenerAdapter.class)
public class DownloadAttachment extends TestSuiteBase
{

	static int count=-1;
	static boolean skip=false;
	static boolean pass=false;
	static boolean fail=false;
	static boolean isTestPassed=true;
	static boolean isLoginSuccess=false;
	String comments=null;
	String runmodes[]=null;
	ArrayList<Credentials> versionLead;
	ArrayList<Credentials> testManager;
	ArrayList<Credentials> tester;
	static String downloadHandle;
	
	@BeforeTest
	public void checkTestSkip()
	{
		
		if(!TestUtil.isTestCaseRunnable(TM_attachmentsSuiteXls,this.getClass().getSimpleName()))
		{
			APP_LOGS.debug("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//logs
			throw new SkipException("Skipping Test Case"+this.getClass().getSimpleName()+" as runmode set to NO");//reports
		}
		runmodes=TestUtil.getDataSetRunmodes(TM_attachmentsSuiteXls, this.getClass().getSimpleName());
	}
	
	@Test(dataProvider = "getTestData")
	public void VerifyEditAttachment(String role, String GroupName,String PortfolioName,String ProjectName, String Version,
			String EndMonth, String EndYear, String EndDate, String VersionLead, String TestPassName, String TP_TestManager, 
			String TP_EndMonth, String TP_EndYear,String TP_EndDate, String TesterName, String TesterRole, 
			String TestCase, String TestStep, String TestStepExpectedResult, String AttachmentName, 
			String Description, String FileName, String AttachmentSavedMessage, String DownloadExpectedResult) throws Exception
		{
		    // test the runmode of current dataset
			count++;
			if(!runmodes[count].equalsIgnoreCase("Y"))
			{
				skip=true;
				throw new SkipException("Runmode for test set data set to no "+count);
			}
			
			//version lead
			 int versionlead_count = Integer.parseInt(VersionLead);
			 versionLead=new ArrayList<Credentials>();
			 versionLead = getUsers("Version Lead", versionlead_count);
			 
			//TestManager 
			 int testManager_count = Integer.parseInt(TP_TestManager);
			 testManager=new ArrayList<Credentials>();
			 testManager = getUsers("Test Manager", testManager_count);
			 
			 //Tester 
			 int tester_count = Integer.parseInt(TesterName);
			 tester=new ArrayList<Credentials>();
			 tester = getUsers("Tester", tester_count);
			 
			 
			 APP_LOGS.debug(" Executing Test Case -> EditAttachmentFields ");
				
			 System.out.println(" Executing Test Case -> EditAttachmentFields ");				
				
			 APP_LOGS.debug("Opening Browser... ");
				
			System.out.println("Opening Browser... ");
				
			openBrowser();
			
			System.out.println("************** Old Version Lead Login******************");
			isLoginSuccess = login(role);
			
			if (isLoginSuccess) 
			{
	                  		//click on testManagement tab
							APP_LOGS.debug(this.getClass().getSimpleName()+ " Clicking on Test Management Tab");
							getElement("UAT_testManagement_Id").click();
							Thread.sleep(3000);
							
							if(!createProject(GroupName, PortfolioName, ProjectName, Version, EndMonth, EndYear, EndDate, versionLead.get(0).username))
	             			{
	             				fail=true;
	             				APP_LOGS.debug("Project is not created successfully");
	             				comments= "Fail- Project not Created Successfully. ";
	             				TestUtil.takeScreenShot(this.getClass().getSimpleName(), "ProjectCreationUnsuccessfull");
	            				closeBrowser();
	            				
	             				throw new SkipException("Project is not created successfully ... So Skipping all tests in Attachment Suite");
	             			}
                 
							APP_LOGS.debug(" Project Created Successfully.");
							comments= "Pass- Project Created Successfully. ";
							
							closeBrowser();
							
							
							
							APP_LOGS.debug("Opening Browser... ");
							openBrowser();
							
							if(login(versionLead.get(0).username,versionLead.get(0).password, "Version Lead"))
							{
								//click on testManagement tab
								APP_LOGS.debug(this.getClass().getSimpleName()+ " Clicking on Test Management Tab");
								getElement("UAT_testManagement_Id").click();
								Thread.sleep(3000);
								
								//create test pass
		                     	 if(!createTestPass(GroupName, PortfolioName, ProjectName, Version, TestPassName, EndMonth, EndYear, EndDate, testManager.get(0).username))
		                     	 {
		                     		     fail=true;
			                        	 APP_LOGS.debug("Test Pass is not created successfully");
			                        	 comments+="Test Pass not Created Successfully(Fail). ";
			         					 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestPassCreationUnsuccessfull");
			         					 closeBrowser();
			         					 
			                			 throw new SkipException("Test Pass is not created successfully ... So Skipping all tests in Attachment Suite");
		                     	 }
		                        
		                     	APP_LOGS.debug("Test Pass Created Successfully.");
		        				comments+="Pass- Test Pass Created Successfully. ";
              
		        				closeBrowser();
	    					
		        				APP_LOGS.debug("Opening Browser... ");
			 				
		        				System.out.println("Opening Browser... ");
			 				
			 				
		        				openBrowser();
		        				if(login(testManager.get(0).username,testManager.get(0).password, "Test Manager"))
		        				{
					 				//click on testManagement tab
					 				APP_LOGS.debug(this.getClass().getSimpleName()+ " Clicking on Test Management Tab");
					 				getElement("UAT_testManagement_Id").click();
					 				Thread.sleep(2000);
					 				
									if(!createTester(GroupName, PortfolioName, ProjectName, Version, TestPassName, tester.get(0).username, TesterRole, TesterRole))
									{
										 fail=true;
			                        	 APP_LOGS.debug("Tester is not created successfully");
			                        	 comments+="Tester not Created Successfully(Fail). ";
			         					 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TesterCreationUnsuccessfull");
			         					 closeBrowser();
			         					 
			                			 throw new SkipException("Tester is not created successfully ... So Skipping all tests in Attachment Suite");
									}
									
									APP_LOGS.debug("Tester Created Successfully.");
			        				comments+="Pass- Tester Created Successfully. ";
			        				
									if(!createTestCase(GroupName, PortfolioName, ProjectName, Version, TestPassName, TestCase))
									{
										 fail=true;
			                        	 APP_LOGS.debug("Test Case is not created successfully");
			                        	 comments+="Test Case not Created Successfully(Fail). ";
			         					 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseCreationUnsuccessfull");
			         					 closeBrowser();
			         					 
			                			 throw new SkipException("Test Case is not created successfully ... So Skipping all tests in Attachment Suite");
									}
									
									APP_LOGS.debug("Test Case Created Successfully.");
			        				comments+="Pass- Test Case Created Successfully. ";
			        				
									if(!createTestStep(GroupName, PortfolioName, ProjectName, Version, TestPassName, TestStep, TestStepExpectedResult, TestCase, TesterRole))
									{
										 fail=true;
			                        	 APP_LOGS.debug("Test Step is not created successfully");
			                        	 comments+="Test Step not Created Successfully(Fail). ";
			         					 TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepCreationUnsuccessfull");
			         					 closeBrowser();
			         					 
			                			 throw new SkipException("Test Step is not created successfully ... So Skipping all tests in Attachment Suite");
									}
									
									APP_LOGS.debug("Test Step Created Successfully.");
			        				comments+="Pass- Test Step Created Successfully. ";
									
									try
					                 {
					                	
										getElement("TM_attachmentTab_Id").click();
										
				                        //creating an attachment
				                        //createAttachment(GroupName, PortfolioName, ProjectName, Version, TestPassName, TestCase, AttachmentName, Description, TestStep, FileName, TestStepExpectedResult, TesterRole, AttachmentSavedMessage);
										
										dropDownSelect(getElement("TestPassUpperRibbon_groupDropDown_Id"), getElement("TestPassUpperRibbon_GroupList_Id"), GroupName );
									       
								        dropDownSelect(getElement("TestPassUpperRibbon_PortfolioDropDown_Id"), getElement("TestPassUpperRibbon_PortfolioList_Id"), PortfolioName );
									       
									    dropDownSelect(getElement("TestPassUpperRibbon_projectDropDown_Id"), getElement("TestPassUpperRibbon_ProjectList_Id"), ProjectName );
									      
									    dropDownSelect(getElement("TestPassUpperRibbon_versionDropDown_Id"), getElement("TestPassUpperRibbon_VersionList_Id"), Version );
									      
									    dropDownSelect(getElement("TestCaseUpperRibbon_testPassDropDown_Id"), getElement("TestCaseUpperRibbon_TestPassList_Id"), TestPassName );
										
										
									    //selecting test case
								       APP_LOGS.debug("Selecting test case:" +TestCase);
								       getElement("Attachments_testCaseDropDown_Id").click();
								       List<WebElement> testCaseElements = getElement("Attachments_testCaseDropDown_Id").findElements(By.tagName("option"));
									    
									   for(int i =0 ;i<testCaseElements.size();i++)
									   {
										       if(assertTrue(testCaseElements.get(i).getText().equals(TestCase)))
											    {
											       testCaseElements.get(i).click();
											       APP_LOGS.debug(" Correct " +TestCase+ " selected. ");
											       System.out.println(" Correct " +TestCase+ " selected. ");
											       comments+="Pass- Correct " +TestCase+ " selected. ";
											       break;
									            }
										       else
										       {
										    	   fail=true;
										    	   APP_LOGS.debug(TestCase+ " not available in dropdown ");
										    	   comments+="Fail- "+TestCase+ " not available in dropdown ";
										    	   TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestCaseNotAvailable");
										       }
									   }
			                        
										
									    APP_LOGS.debug("Clicking on Create New link");
										getObject("Attachments_createNew_link").click();
										Thread.sleep(500);
										    
									    APP_LOGS.debug("Inputing attachment name:" +AttachmentName);
									    getElement("AttachmentsCreateNew_attachmentNameTextField_Id").sendKeys(AttachmentName);
									    
													
										APP_LOGS.debug("Inputing attachment description:" +Description);
										getElement("AttachmentsCreateNew_attachmentDescriptionTextField_Id").sendKeys(Description);
										
													    
										APP_LOGS.debug("Uploading file:" +FileName);
										getElement("AttachmentsCreateNew_attachmentFileNameBrowseButton_Id").sendKeys(System.getProperty("user.dir")+"\\Attachments\\"+FileName);
										   
										
										APP_LOGS.debug("Selecting test step:" +TestStep);
										getObject("AttachmentsCreateNew_testStepNameInputXpath1", "AttachmentsCreateNew_testStepNameInputXpath2", 1).click();
										
										
										APP_LOGS.debug("Clicking on Save button");
									    try
									    {
										    getElement("AttachmentsCreateNew_saveButton_Id").click();
										    Thread.sleep(3000);
									    }
									    catch(Throwable t)
									    {
									    	fail=true;
									    	APP_LOGS.debug("Save button not clickable");
								    	    comments+="Fail- Save button not clickable";
									    	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "SaveAttachmentOperationError");
									    	
									    }
			                          
									    //verify alert message
									    String actual_SaveAttachment_SuccessMessage = getElement("AttachmentsCreateNew_attachmentSuccessMessageText_Id").getText();
										if(compareStrings(AttachmentSavedMessage, actual_SaveAttachment_SuccessMessage))   
										{
											APP_LOGS.debug(AttachmentName+" Save successfully");
											comments+="Pass- "+AttachmentName+" Save successfully";
											getObject("AttachmentsCreateNew_attachmentSuccessfullOkButton").click();  
											Thread.sleep(1000);
										}
									    
										else
										{
											fail=true;
											APP_LOGS.debug(AttachmentName+" is not Saved");
								    	    comments+="Fail- "+AttachmentName+" is not Saved";
									    	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "AttachmentNotSavedError");
										}
									    
									    
				                        //Extracting number of rows and columns from View All Project table		
				                		APP_LOGS.debug("Extracting Number of attachments present in Attachment grid");
				                		WebElement attachmentTable = getObject("AttachmentsViewAll_table");
				                		List<WebElement> attachmentRows = attachmentTable.findElements(By.tagName("tr"));
				                		System.out.println("Attachment row size: "+attachmentRows.size());
				                	    for (int i =  1; i <=attachmentRows.size(); i++) 
				                	    {	
				                	    	//Extracting the number of project present in Project column
				                	    	WebElement attachmentTableColsXpath = getObject("AttachmentsViewAll_tableXpath1", "AttachmentsViewAll_tableXpath2", i);
				                			
				                	    	String attachmentNameColTitle = attachmentTableColsXpath.getText();
				                			
				                			if(assertTrue(attachmentNameColTitle.equals(AttachmentName)))
				                			{
				                				APP_LOGS.debug("Clicking on Edit Icon...");
				                				getObject("AttachmentsViewAll_editImageXpath1", "AttachmentsViewAll_editImageXpath2", i).click();
				                				Thread.sleep(1000);
				                			}
				                			else
				                			{
				                				fail=true;
				                				APP_LOGS.debug(AttachmentName+" not found in table....so not able to click on it");
									    	    comments+="Fail- "+AttachmentName+" not found in table name....so not able to click on it";
										    	TestUtil.takeScreenShot(this.getClass().getSimpleName(), "AttachmentNameNotVisibleInTable");
				                			}
				                	    }
			                        
			                            //clicking on Download button
				                		try
				                		{
				                		    APP_LOGS.debug("Clicking on Download button");
				                		 
				                		    eventfiringdriver.executeScript("$('#attDownload').click()");
				                		    APP_LOGS.debug(" Download button is clicked");
								    	    comments+="Pass- Download button is clicked";
				                		    Thread.sleep(2000);
				                		    
				                		    APP_LOGS.debug("Redirecting to the new window");
				                		    
				                		    downloadHandle = eventfiringdriver.getWindowHandle();
				                		    
				                		    APP_LOGS.debug("Verifying file name in new window");
				                			openInNewWindow(downloadHandle, FileName);
				            	                			
				                		}
				                		catch(Throwable t)
				                		{
				                			  t.printStackTrace();
				                			  APP_LOGS.debug(" Download button not clickable");
									    	  comments+="Fail- Download button not clickable";
				                		      TestUtil.takeScreenShot(this.getClass().getSimpleName(), "VerifyDownloadOperationFailed");
				                		}
			                		
			                		
	                 
	                
				                 }
								catch(Throwable t)
								{
									t.printStackTrace();
									fail = true;
									APP_LOGS.debug("Exception in Test Step Tab");
									comments+="Exception in Test Step Tab. ";
									TestUtil.takeScreenShot(this.getClass().getSimpleName(), "TestStepTabException");
									assertTrue(false);
									closeBrowser();
									
								}	
									
			        }
						    else 
							{
								isLoginSuccess=false;
								APP_LOGS.debug("Login Unsuccessfull");
								comments+="Login Unsuccessfull";
							}
				}	
							
				else
				{
					isLoginSuccess = false;
					APP_LOGS.debug("Login Unsuccessfull");
					comments+="Login Unsuccessfull";
					
				}
			}
			
			else
			{
				isLoginSuccess = false;
				APP_LOGS.debug("Login Unsuccessfull");
				comments+="Login Unsuccessfull";
				
			}
	}
	
	@DataProvider
	public Object[][] getTestData()
	{
		return TestUtil.getData(TM_attachmentsSuiteXls, this.getClass().getSimpleName());
	}

	@AfterMethod
	public void reportDataSetResult()
	{
		if(skip)
			TestUtil.reportDataSetResult(TM_attachmentsSuiteXls, this.getClass().getSimpleName(), count+2, "SKIP");
		else if(!isLoginSuccess){
			isTestPassed=false;
			TestUtil.reportDataSetResult(TM_attachmentsSuiteXls, this.getClass().getSimpleName(), count+2, "Login UnSuccessful");
			TestUtil.printComments(TM_attachmentsSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		}
		else if(fail)
		{
			isTestPassed=false;
			TestUtil.reportDataSetResult(TM_attachmentsSuiteXls, this.getClass().getSimpleName(), count+2, "FAIL");
			TestUtil.printComments(TM_attachmentsSuiteXls, this.getClass().getSimpleName(), count+2, comments);
			
		}
		else
		TestUtil.reportDataSetResult(TM_attachmentsSuiteXls, this.getClass().getSimpleName(), count+2, "PASS");
		TestUtil.printComments(TM_attachmentsSuiteXls, this.getClass().getSimpleName(), count+2, comments);
		skip=false;
		fail=false;
	}
	
	@AfterTest
	public void reportTestResult() throws InterruptedException
	{
		if(isTestPassed){
			TestUtil.reportDataSetResult(TM_attachmentsSuiteXls, "Test Cases", TestUtil.getRowNum(TM_attachmentsSuiteXls,this.getClass().getSimpleName()), "PASS");
		    TestUtil.printComments(TM_attachmentsSuiteXls, "Test Cases", TestUtil.getRowNum(TM_attachmentsSuiteXls,this.getClass().getSimpleName()), comments);
		}
		    else{
			TestUtil.reportDataSetResult(TM_attachmentsSuiteXls, "Test Cases", TestUtil.getRowNum(TM_attachmentsSuiteXls,this.getClass().getSimpleName()), "FAIL");
		    TestUtil.printComments(TM_attachmentsSuiteXls, "Test Cases", TestUtil.getRowNum(TM_attachmentsSuiteXls,this.getClass().getSimpleName()), comments);
		}
     }
	
			
	//common code opening image in new browser window
	 public static void openInNewWindow(String parentHandleName, String fileName)
	 {
		 try
		 {	
		     // call the method numberOfWindowsToBe as follows
		     WebDriverWait wait = new WebDriverWait(eventfiringdriver, 15, 100);
		     wait.until(numberOfWindowsToBe(2));
		     Set<String> availableWindows = eventfiringdriver.getWindowHandles();//this set size is
		     
		     // returned as 1 and not 2
		      String newWindow = null;
		      for (String window : availableWindows) {
		           	  if (!parentHandleName.equals(window)) {
		              newWindow = window;
		          }
		      }

		      // switch to new window
		      eventfiringdriver.switchTo().window(newWindow);
		      eventfiringdriver.manage().window().maximize();
		      Thread.sleep(1000);
		      String getURL = eventfiringdriver.getCurrentUrl();
		      
		      if(getURL.contains(fileName))
		      {
		    	  APP_LOGS.debug(fileName+": is available in url : " + getURL);
		    	  System.out.println(fileName+": is available in url : " + getURL);
		      }
		      // and then close the new window
		      eventfiringdriver.close();
		      
		      // switch to parent
		      eventfiringdriver.switchTo().window(parentHandleName);
		     
		 }
		 catch(Throwable t)
		 {
			 t.printStackTrace();
		 }
	 }
	 
	 
	//code for no of windows 
		 public static ExpectedCondition<Boolean> numberOfWindowsToBe(final int numberOfWindows) {
		     return new ExpectedCondition<Boolean>() {
		       @Override
		       public Boolean apply(WebDriver driver) {
		        eventfiringdriver.getWindowHandles();
		         return eventfiringdriver.getWindowHandles().size() == numberOfWindows;
		       }
		     };
		  
		 }
	
}
