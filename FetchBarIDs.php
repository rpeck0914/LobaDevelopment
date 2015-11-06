<?php
    $con=mysqli_connect("mysql10.000webhost.com","a4937391_loba","loba54dev","a4937391_loba");
	
	$cityid = $_POST["cityid"];
	//$cityid = 1;
	
	$statement = mysqli_prepare($con, "SELECT * FROM BarDetails WHERE city_id = ?");
	mysqli_stmt_bind_param($statement, "i", $cityid);
	mysqli_stmt_execute($statement);
	
	mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement, $barid, $cityid, $barname, $baraddress, $barzipcode, $barphone);
	
	$barids = array();
	$counter = 0;
	
	while(mysqli_stmt_fetch($statement)) {
		$barids[$counter] = $barid;
		$counter = $counter + 1;
	}
	
	echo json_encode($barids);
	
	mysqli_close($con);
?>