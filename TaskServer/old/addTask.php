<?PHP
    include("database.php");
    
    $id = $_POST['id'];
    
    $name = $_POST['name'];
    $description = $_POST['description'];
    
    $complete = $_POST['complete'];
    
    $comment = $_POST['comment'];
    $photo = $_POST['photo'];
    $audio = $_POST['audio'];
    
    $userid = $_POST['userid'];
    
    $mysqli = new mysqli(SERVER, DB_USER, DB_PASSWORD, DB, DB_PORT);
    
    if ($mysqli->connect_errno) {
        die();
    }
    
    
    $query = sprintf("SELECT * FROM tasks WHERE id = '%s'", $id);
    $res = $mysqli->query($query);
    
    if ($res->num_rows > 0) {
        $query = sprintf("UPDATE tasks SET userid = '%s', name = '%s', \
                         complete = '%s', comment = '%s', photo = '%s', \
                         audio = '%s' WHERE id = '%s'",
                         $userid, $name,  $description, $complete,
                         $comment, $photo, $audio, $id);
    } else {
        $query = sprintf("INSERT INTO tasks (userid, name, description, complete, \
                         comment, photo, audio) VALUES ('%s', '%s', '%s', \
                         '%s', '%s', '%s', '%s');",
                         $userid, $name,  $description, $complete,
                         $comment, $photo, $audio);
    }
                        
    $res = $mysqli->query($query);
    $res->data_seek(0);
    $row = $res->fetch_assoc();
    
    echo $row['id'];

?>