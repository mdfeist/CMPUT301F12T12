<?PHP
    
    include("database.php");
    
    $mysqli = new mysqli(SERVER, DB_USER, DB_PASSWORD, DB, DB_PORT);
    
    if ($mysqli->connect_errno) {
        die();
    }
    
    $res = $mysqli->query("SELECT * FROM tasks");
    
    $res->data_seek(0);
    while ($row = $res->fetch_assoc()) {
        echo " id = " . $row['id'] . "\n";
    }
?>