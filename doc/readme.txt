//***************************************************************//
//****    DEPLOYMENT                      **********************//
//**************************************************************//

Listeners are deployed under 
	/apps/britebin/listeners
	
There are 2 listeners
	BriteBinTcpListener.jar
	TekelekTcpListener.jar
	
There are 2 bash script files for running these listener
	runBriteBin.sh
	runTekelek.sh
	
Don't forget to make these executable
	chmod +x filename.sh

	
There are 2 services defined in /etc/systemd/system
	britebin-listener.service
	tekelek-listener.service

These services are enabled - they start on system reboot and restart after a failure

To deploy a new version of a listener service
1. Stop the service
	systemctl stop <service-name>
2. Copy the file onto the server
3. Start the service
	systemctl start <service-name>
4. Enable the service (so it restarts on boot)
	systemctl enable <service-name>
5. Reload all dependencies
	systemctl daemon-reload
4. Check if service is running
	systemctl list-units --type service --all


////////////////////////////////////////////
/////// LOG FILES /////////////////////////
///////////////////////////////////////////
Note: log files for both listeners can be found in /apps/britebin/listeners/logs



////////////////////////////////////////
// OLD WAY - I could NOT get working ///
////////////////////////////////////////
Goto apps/BriteBin

There is a script run.sh that has 2 forever commands
	forever start -l britebin_forever.log -o britebin_out.log -e britebin_err.log ./runBriteBinListener.sh
	forever start -l tekelek_forever.log -o tekelek_out.log -e tekelek_err.log ./runTekelekListener.sh
	
Each of the script files forever runs starts each of the TcpListeners which are run from java jar files



To make sure the Listeners start after a server re-boot, we can add them to the crontab file. To do this

	1. To edit the file
		crontab -e
	2. Put the 2 lines above in he file (make sure to include the paths)
		@reboot forever start -l britebin_forever.log -o britebin_out.log -e britebin_err.log ~/apps/BriteBin/runBriteBinListener.sh
		@reboot forever start -l tekelek_forever.log -o tekelek_out.log -e tekelek_err.log ~/apps/BriteBin/runTekelekListener.sh
		
Check where cd /apps