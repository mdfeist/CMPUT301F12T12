<?PHP
    
    include("database.php");
    
    $username = $_POST['username'];
    
    $mysqli = new mysqli(SERVER, DB_USER, DB_PASSWORD, DB, DB_PORT);
    
    if ($mysqli->connect_errno) {
        die();
    }
    
    $query = sprintf("SELECT * FROM users WHERE username = '%s'", $username);
    $res = $mysqli->query($query);
    
    if ($res->num_rows == 0) {
        echo '{"errorCode": "1", "errorMessage": "Unable to find user"}';
        die();
    }
    
    $res->data_seek(0);
    $user = $res->fetch_assoc();
    
    echo '{"user": "' . $user['username'] . '", "email": "' . $user['email'] .'"}';
    die();
?>