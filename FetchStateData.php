<?php
    $con=mysqli_connect("mysql10.000webhost.com","a4937391_loba","loba12dev","a4937391_loba");
	
	//$goForFetch = $_POST["go"];
	
	$statement = mysqli_prepare($con, "SELECT * FROM StateDetails");
	mysqli_stmt_execute($statement);
	
	mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement, $stateID, $statename);
	
	$state = array();
	$stateid = array();
	$counter = 0;
	
	while(mysqli_stmt_fetch($statement)) {
		$state[$stateID] = $statename;
		//$stateid[$counter] = $stateID;
		$counter = $counter + 1;
	}
	
	//return json_encode($state);
	
	echo json_encode($state);
	//echo json_encode($stateid);
	
	mysqli_close($con);
?>