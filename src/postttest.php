<?php
$servername = "localhost";
$username = "root";
$password = "MyNewPass";
$dbname = "moodle";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 
//$sql = "SELECT mdl_enrol.id,  mdl_enrol.enrol, mdl_enrol.courseid, mdl_course.fullname FROM moodle.mdl_enrol INNER JOIN mdl_course ON mdl_enrol.courseid = mdl_course.id";
//$sql = "SELECT * FROM mdl_user , mdl_user_enrolments, mdl_enrol WHERE mdl_user.id = mdl_user_enrolments.userid AND mdl_user_enrolments.enrolid = mdl_enrol.id AND mdl_enrol.courseid = mdl_course.id";
//$sql = "SELECT DISTINCT mdl_user.id, mdl_user.username, mdl_user.lastname, mdl_user_enrolments.enrolid , mdl_enrol.courseid, mdl_enrol.id, mdl_course.fullname, mdl_enrol.enrol  FROM  mdl_user , mdl_user_enrolments, mdl_enrol, mdl_course WHERE mdl_user.id = mdl_user_enrolments.userid AND mdl_user_enrolments.enrolid = mdl_enrol.id AND mdl_enrol.courseid = 2 AND mdl_course.id = 2 AND mdl_enrol.enrol = 'manual'";




$sql = "";
$respocetype;
if($_POST["requestType"] == "course")
{
	$respocetype = array("type"=>"course");
	$sql = "SELECT mdl_course.id, mdl_course.fullname FROM mdl_course";	
}

elseif($_POST["requestType"] == "student" )
{
	//print($_POST["courseName"]);
	$sql = "SELECT mdl_course.id FROM mdl_course WHERE mdl_course.fullname = '" . $_POST["courseName"] . "'";
	//echo($sql);
	$result = $conn->query($sql);
	if ($result->num_rows > 0) {
		while($r = mysqli_fetch_assoc($result)) {
			//print("<br>");
			//print $r["id"];
			//print("<br>");
			$respocetype = array("type"=>"student");
			$sql = "SELECT DISTINCT mdl_role_assignments.roleid, mdl_user.id, mdl_user.username, mdl_user.firstname, mdl_user.lastname, mdl_user_enrolments.enrolid , mdl_enrol.courseid, mdl_enrol.id, mdl_course.fullname, mdl_enrol.enrol  FROM mdl_role_assignments, mdl_user , mdl_user_enrolments, mdl_enrol, mdl_course WHERE mdl_user.id = mdl_user_enrolments.userid AND mdl_user_enrolments.enrolid = mdl_enrol.id AND mdl_enrol.courseid = " . $r["id"] . " AND mdl_course.id = " . $r["id"] . " AND mdl_enrol.enrol = 'manual'  AND mdl_role_assignments.userid = mdl_user.id AND mdl_role_assignments.roleid = 5";

		}
		$json = json_encode($rows);
		//print("<hr>");
		//echo $json;
	} 
	else {
		echo "<br>(0) results";
	}
	
	
}


$result = $conn->query($sql);
// $json = json_encode($result);






if ($result->num_rows > 0) {
    // output data of each row
	    $rows = array();
		$rows[] = $respocetype;
	while($r = mysqli_fetch_assoc($result)) {
	    $rows[] = $r;
		//print_r ($r);
		//echo("<br>");
	}
	$json = json_encode($rows);
	//print("<hr>");
	echo $json;
} 
else {
    echo "0 results";
}
$conn->close();
?>