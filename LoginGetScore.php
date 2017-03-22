<?php
   $con=mysqli_connect("localhost","suplex","XYZ","suplex"); // XYZ ist nicht das richtige Passwort für die Datenbank

   if (mysqli_connect_errno($con)) {
      echo "Failed to connect to MySQL: " . mysqli_connect_error();
   }
   
   $username = $_POST['username'];
   $password = $_POST['password'];
   $result = mysqli_query($con,"SELECT score FROM InfoProjekt where username='$username' and password='$password'");
   $row = mysqli_fetch_array($result);
   $data = $row[0];

   if($data){
      echo $data;
   }
   
   mysqli_close($con);
?>
