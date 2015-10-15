<?php
    $con=mysqli_connect("mysql10.000webhost.com","a4937391_loba","loba12dev","a4937391_loba");
	
	$stateid = $_POST["stateid"];
	//$stateid = 21;
	
	$statement = mysqli_prepare($con, "SELECT * FROM CityDetails WHERE state_id = ?");
	mysqli_stmt_bind_param($statement, "i", $stateid);
	mysqli_stmt_execute($statement);
	
	mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement, $cityID, $cityname, $stateID);
	
	$city = array();
	//$cityid = array();
	$counter = 0;
	
	while(mysqli_stmt_fetch($statement)) {
		$city[$cityID] = $cityname;
		//$cityid[$counter] = $cityID;
		//$counter = $counter + 1;
	}
	
	echo json_encode($city);
	//echo json_encode($cityid);
	
	 mysqli_close($con);
?>