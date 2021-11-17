<?php /* 
      //export.php  
      
//////this File is made to find the names of students and their attendance //
      
 $servername = "localhost";
$username = "root";
$password = "";
$dbname = "moodle";
$value11=$_POST["owner"];
//$value21=$_POST["owner1"];
 if(isset($_POST["export"]))  
 {  
    // echo $value11;
     //echo $value21; 
     $conn = new mysqli($servername, $username, $password, $dbname);  
      header('Content-Type: text/csv; charset=utf-8');  
      header('Content-Disposition: attachment; filename=data.csv');  
      $output = fopen("php://output", "w"); 
      $query1= "SELECT COLUMN_NAME FROM Information_schema.columns where table_name='".$value11."'"; 
      //$dat=array('ID', 'Name', 'Address', 'Gender', 'Designation', 'Age');
      $result1 = $conn->query($query1);
      //echo $result1;
      // $ab1="Select SUM(totalattendance) as a from".$value11."";
      // $ab11 = $conn->query($ab1);
       //$row10 = mysqli_fetch_assoc($ab11);
      
	  while($row1 = mysqli_fetch_assoc($result1))  
      {  //echo '/n';
			
			$array1=array();
			$total[]=array();
		//echo ($row1['COLUMN_NAME']);
           $array1[]=$row1['COLUMN_NAME'];               //COLUMN NAME 
		  // echo ($array1[0]);
          // fputcsv($output,$array1);
	   	   //fputcsv($output,$array1);
	   	 
		   $query= "SELECT ".$array1[0]." as F  FROM ".$value11."";      //values of that column;
           $result = mysqli_query($conn, $query);  
		//$array1=array();		  
		  while($row = mysqli_fetch_assoc($result))  
		   {    		  
			//$array1=array();
           $array1[]=$row['F'];
		  // $total[]=$row['G'];
           // echo '/n';
		  // fputcsv($output,$array1);
			   //fputcsv($output, $row);    
		  }
		  
		  //echo $array1[];
		  //echo $array1;
		  
		  if(strcmp($row1['COLUMN_NAME'],"teachername")==0 )
		  {
			  $array1[]='Total';
		  }
		  else if(strcmp($row1['COLUMN_NAME'],"lectureno")==0 || strcmp($row1['COLUMN_NAME'],"mobiletableid")==0 || strcmp($row1['COLUMN_NAME'],"teachername")==0 || strcmp($row1['COLUMN_NAME'],"ENTRYTIME")==0)
		  {
			  
		  }
		  else
		  {
			//echo count($array1);
			  $total=0;
			  for( $i=1;$i<count($array1);$i++)
			  {
				  $total=$total+$array1[$i]; 
			  }
			 // {
			//	  $total=$total+$array1[i];
			 // }
			  //echo array_sum($total);
			  $array1[]=$total;
		  }
		  
           fputcsv($output,$array1);
			 
      }
	   fclose($output);
      //fputcsv($output,$array1);
     /* $query= "SELECT * FROM ".$value11."";
      $result = mysqli_query($conn, $query);  
      while($row = mysqli_fetch_assoc($result))  
      {    		  
           fputcsv($output, $row);    
		   fclose($output);  
	  } 
		*/
 //}	
 ?>  