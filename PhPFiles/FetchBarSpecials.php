<?php
    $con=mysqli_connect("mysql10.000webhost.com","a4937391_loba","loba54dev","a4937391_loba");
	
	$barid = $_POST["barid"];
	//$barid = 1;
	
	$statement = mysqli_prepare($con, "SELECT * FROM BarSpecials WHERE bar_id = ?");
	mysqli_stmt_bind_param($statement, "i", $barid);
	mysqli_stmt_execute($statement);
	
	mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement, $barid, $fri, $sat, $sun, $mon, $tue, $wed, $thur);
	
	$barspecials = array();
	
	while(mysqli_stmt_fetch($statement)) {
		$barspecials["fri"] = $fri;
		$barspecials["sat"] = $sat;
		$barspecials["sun"] = $sun;
		$barspecials["mon"] = $mon;
		$barspecials["tue"] = $tue;
		$barspecials["wed"] = $wed;
		$barspecials["thur"] = $thur;
	}
	
	echo json_encode($barspecials);
	
	mysqli_close($con);
?>