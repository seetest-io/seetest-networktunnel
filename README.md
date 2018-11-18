# seetest.io - Demonstrates the Network Tunneling

This Project demonstrates how an Automated test which uses Local Application can be tested against seetest cloud using Network Tunnelling.

This example will cover:

1. Test which run against seetest cloud.
2. This test packages an Embedded Hello World Web Server and starts it.
4. Seetest Tunnel is started from the test so that Tunneling is established
5. A browser instance is then loaded in a chosen device in seetest cloud which contacts local machine's webserver from cloud.
6. Operations/Asserts are performed on the Simple Hello World Application.


### Steps to run demo test

1. Clone this git repository

	```
	git clone 
	```

2. Obtain access key from seetest.io cloud

    https://docs.seetest.io/display/SEET/Execute+Tests+on+SeeTest+-+Obtaining+Access+Key

    note :  you need to have a valid subscription or a trial to run this test (Trial \ paid)

3. Upload the eribank application to your project
    Download the Android app : https://experitest.s3.amazonaws.com/eribank.apk
    Download the iOS app : https://experitest.s3.amazonaws.com/EriBank.ipa

    Go to the cloud "Mobile Application Center" and upload both apps 
    https://cloud.seetest.io/index.html#/applications

4. Follow the video to download the Seetest Network Tunneling Executable and save it in local machine.

   ![Scheme](images/TunnelDownload.gif)


5. Configure path of the downloaded network_tunnwel.exe in src/main/java/resources/seetest.properties.
s
    ```
    seetest.network.tunnelpath=<full_path of Downloaded Network_Tunnel.exe>.
    ```

6. To run the tests,
    
    Please ensure that following environment variables are set.

    1. JAVA_HOME to JDK/JRE HOME and update it in the PATH variable.
    2. SEETEST_IO_ACCESS_KEY to valid access key obtained before in Step 2.

        In Windows:

        ```
        set SEETEST_IO_ACCESS_KEY=<your access key>
        ```

        In Unix:

        ```
        export SEETEST_IO_ACCESS_KEY=<your access key>
        ```

    Now run the tests using following command in command line.
    
	```
	gradlew runTests
	```

### How to change to your own application

1. Upload you application to the cloud

   (review step two in guide)

2. Change the android application name or iOS application name in the src/main/java/resources/seetest.properties file

    For IOS,
    
	```
	ios.app.name = com.company.app
	``` 
    
    For Android,
    
    ```
    android.app.name = com.company.app/.appActivity
    ```

3. Modify the tests

Change the @Test methods in EriBankTests source.

You can paste the code you've exported from SeeTestAutomation

