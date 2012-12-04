<?PHP
    
    include("database.php");
    
    $username = $_POST['username'];
    $password = $_POST['password'];
    
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
    
    if ($user['password'] != md5($password)) {
        echo '{"errorCode": "2", "errorMessage": "Incorrect passward"}';
        die();
    }
    
    echo '{"username": "' . $user['username'] . '", "email": "' . $user['email'] .'"}';
    die();
    
    ?>