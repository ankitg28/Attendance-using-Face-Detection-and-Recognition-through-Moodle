<?php  
      //export.php  
 $servername = "localhost";
$username = "root";
$password = "";
$dbname = "moodle";
$value11=$_POST["owner"]; //course name
$value21=$_POST["owner1"]; // % lessthan
 if(isset($_POST["export"]))  
 {   
     $conn = new mysqli($servername, $username, $password, $dbname);  
      header('Content-Type: text/csv; charset=utf-8');  
      header('Content-Disposition: attachment; filename=data.csv');  
      $output = fopen("php://output", "w"); 
      $query1= "SELECT COLUMN_NAME FROM Information_schema.columns where table_name='".$value11."'"; 
      //$dat=array('ID', 'Name', 'Address', 'Gender', 'Designation', 'Age');
      $result1 = $conn->query($query1);
      //echo $result1;
       $ab1="SELECT SUM(totalattendance) as a from ".$value11." ";
       $ab11 = $conn->query($ab1);
       $row10 = mysqli_fetch_assoc($ab11);
       //echo $row10['a'] ;
       //fputcsv($output,$array1);
       //fclose($output); 
       if($row10['a'] ==0)
       {
           echo "NO attendance entries recorded in table";
           return 0;
       }
       else {
           
       
       while($row1 = mysqli_fetch_assoc($result1))  
      {  

          //echo $row1['COLUMN_NAME'];
         
           if(strcmp($row1['COLUMN_NAME'],"lectureno")==0 || strcmp($row1['COLUMN_NAME'],"mobiletableid")==0 || strcmp($row1['COLUMN_NAME'],"teachername")==0 || strcmp($row1['COLUMN_NAME'],"totalattendance")==0 || strcmp($row1['COLUMN_NAME'],"ENTRYTIME")==0)
           {
                   // echo "awdgawgd";
           }
           else
           {
  
                $ab="Select SUM(".$row1['COLUMN_NAME'].") as b from " .$value11."";
                $ab12 = $conn->query($ab);
                 $row11 = mysqli_fetch_assoc($ab12);

                $per=($row11['b'] * 100)/$row10['a'];
                if($per < $value21)
                {
                   $array1[]=$row1['COLUMN_NAME'];
                    
                }
           }
           
           
           //fputcsv($output,$row['COLUMN_NAME'] );
           
      }
      fputcsv($output,$array1); 
      //fputcsv($output,$array1);
      //$query= "SELECT * FROM ".$value11."";
      //$result = mysqli_query($conn, $query);  
      //while($row = mysqli_fetch_assoc($result))  
      //{             
        //   fputcsv($output, $row);  
      //}  
      fclose($output);  
       }
    }  
 ?>  