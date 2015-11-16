<?php
    $con=mysqli_connect("mysql10.000webhost.com","a4937391_loba","loba54dev","a4937391_loba");
	
	//$barid = $_POST["barid"];
	//$dayofweek = $_POST["dayofweek"];
	
	$barid = "1";
	$dayofweek = "Monday";
	
	$statement = mysqli_prepare($con, "SELECT * FROM BarSpecials WHERE bar_id = ? AND dayofweek = ?");
	mysqli_stmt_bind_param($statement, "ss", $barid, $dayofweek);
	mysqli_stmt_execute($statement);
	
	mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement, $barId, $dayofweek, $special, $addedby, $dateforspecial);
	
	$barspecials = array();
	$barspecialsaddedby = array();
	$dateforspecials = array();
	$counter = 0;
	
	while(mysqli_stmt_fetch($statement)) {
		$barspecials[$counter] = $special;
		$barspecialsaddedby[$counter] = $addedby;
		$dateforspecials[$counter] = $dateforspecial;
		$counter = $counter + 1;
	}
	
	echo json_encode($barspecials);
	echo json_encode($barspecialsaddedby);
	echo json_encode($dateforspecials);
	
	mysqli_close($con);
?>