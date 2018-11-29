# seetest.io - Demonstrates the Network Tunneling

This Project demonstrates how an Automated test which uses Local Application can be tested against seetest cloud using Network Tunnelling.


Following steps explains what this Test suite does.

1. This test packages an Embedded Hello World Web Server and starts it on the IP/PORT configured in properties file.
2. Runs the Network Tunnel executable so that tunneling to the seetest cloud is established.
3. A browser instance is then loaded in a chosen device in seetest cloud which contacts local machine's webserver from cloud.
4. Operations/Asserts are performed on the Simple Hello World Application.


### Steps to run demo test

1. Clone this git repository

	```
	git clone
	```

2. Obtain access key from seetest.io cloud

    https://docs.seetest.io/display/SEET/Execute+Tests+on+SeeTest+-+Obtaining+Access+Key

    note :  you need to have a valid subscription or a trial to run this test (Trial \ paid)


3. Follow the video to download the Seetest Network Tunneling Executable and save it in local machine. ![Scheme](images/TunnelDownload.gif)

4. In Linux and Mac hosts, make sure you give execute permission to the downloaded Network Tunneling executable.

5. Configure path of the downloaded network_tunnwel.exe in src/main/resources/seetest.properties.

    ```
    seetest.network.tunnelpath=<full_path of Downloaded Network_Tunnel.exe>
    ```

6. Configure your local network interface card IP to the property local.embedded.host in src/main/java/resources/seetest.properties.

    ```
    local.embedded.host=<your machine ip>
    ```

5. To run the tests,
    
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