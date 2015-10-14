<?php
    $con=mysqli_connect("mysql10.000webhost.com","a4937391_loba","loba12dev","a4937391_loba");
	
	$statement = mysqli_prepare($con, "SELECT * FROM StateDetails");
	mysqli_stmt_execute($statement);
	
	mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement, $stateID, $statename);
	
	$states = array();
	//$stateid = array();
	//$counter = 0;
	
	while(mysqli_stmt_fetch($statement)) {
		$state[$stateID] = $statename;
		//$counter = $counter + 1;
	}
	
	//$test = array_fill_keys($state, "states");
	
	echo json_encode($state);
	
	mysqli_close($con);
?>