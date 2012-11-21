
include("database.php");

$username = $_POST['username'];
$email = $_POST['email'];
$password = $_POST['password'];

$mysqli = new mysqli(SERVER, DB_USER, DB_PASSWORD, DB, DB_PORT);

if ($mysqli->connect_errno) {
    die();
}

$query = sprintf("SELECT * FROM users WHERE username = '%s'", $username);
$res = $mysqli->query($query);

if ($res->num_rows != 0) {
    echo '{"errorCode": "1", "errorMessage": "User already exists"}';
    die();
}

$query = sprintf("INSERT INTO users (username, password, email)'%s'", $username);

md5($password)

echo '{"username": "' . $user['username'] . '", "email": "' . $user['email'] .'"}';
die();