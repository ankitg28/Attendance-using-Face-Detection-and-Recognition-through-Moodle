<?php
//webpage to find short attendance//
//webpage associated with expp.php//  
 $servername = "localhost";
$username = "root";
$password = "";
$dbname = "moodle";
$year="2019_20";
 $conn = new mysqli($servername, $username, $password, $dbname); 
$query= "select concat(a.name,'_',b.fullname,'_','".$year."','_',b.id) as con ,b.id as id from mdl_course_categories as A inner join mdl_course as B on A.id=B.category where B.category in(select id from mdl_course_categories where path like (concat((select path from mdl_course_categories where name='" .$year. "' ),'_%')))";
$result = mysqli_query($conn, $query);  
?>  
 <!DOCTYPE html>  
 <html>  
      <head>  
           <title>Webslesson Tutorial | Export Mysql Table Data to CSV file in PHP</title>  
           <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js"></script>  
           <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" />  
           <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>  
      </head>  
      <body>  
           <br /><br />  
           <div class="container" style="width:900px;">  
                <h2 align="center">Exporting Page</h2>  
                <h3 align="center">Student Data</h3>                 
                <br />  
                <form method="post" action="expp.php" align="center">  
                                <tr>
                <td>Select Course</td>
                <td>
                <select name="owner">
                <?php 
                $sql = mysqli_query($conn, $query);
                while ($row = $sql->fetch_assoc()){
                    echo '<option value="'.$row['con'].'">'.$row['con'].'</option>';


                //echo "<option value=\"owner1\">" . $row['con'] . "</option>";
                }
                ?>
                </select>
                </td>
                </tr>

               <tr>
                <td>Select base to select students</td>
                <td>
                <select name="owner1">
                <?php 
                    echo '<option value="30">30  </option>';
                    echo '<option value="40"> 40</option>';
                    echo '<option value="50"> 50</option>';
                    echo '<option value="60"> 60</option>';
                    echo '<option value="70">70 </option>';
                
                ?>
                </select>
                </td>
                </tr>

                     <input type="submit" name="export" value="CSV Export" class="btn btn-success" />  
                </form>  
                <br />  
                <div class="table-responsive" id="employee_table">  
                     <table class="table table-bordered">  
                          <tr>   
                               <th width="25%">Name</th>  
                               
                          </tr>  
                     <?php  
                     while($row = mysqli_fetch_array($result))  
                     {  
                     ?>  
                          <tr>  
                                
                               <td><?php echo $row["con"]; ?></td>  
                               
                          </tr>  
                     <?php       
                     }  
                     ?>  
                     </table>  
                </div>  
           </div>  
      </body>  
 </html>  