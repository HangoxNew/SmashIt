<?php
   $conn=mysqli_connect("localhost","suplex","XYZ","suplex"); // XYZ ist nicht das richtige Passwort

   if (mysqli_connect_errno($conn)) {
      echo "Failed to connect to MySQL: " . mysqli_connect_error();
   }
   
   $username = $_POST['username'];
   $password = $_POST['password'];
   $score_s	 = $_POST['score'];
   $score    = intval($score_s);

   $sql = "UPDATE `suplex`.`InfoProjekt` SET `score` = '$score',`updated_at` = NOW() WHERE `InfoProjekt`.`username` = '$username' and `InfoProjekt`.`password` = '$password';";

   $conn->query($sql);

   $result = mysqli_query($conn,"SELECT score FROM InfoProjekt where username='$username' and password='$password'");
   $row = mysqli_fetch_array($result);
   $data = $row[0];

    if ($data == $score) {
      echo "ok";
    }
    else
    {
      echo "nope " . $conn->error;
    }
   
   mysqli_close($conn);
?>
