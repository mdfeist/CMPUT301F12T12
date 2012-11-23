<?PHP
    
    require_once("database.php");
    
    $mysqli = new mysqli(SERVER, DB_USER, DB_PASSWORD, DB, DB_PORT);
    
    if ($mysqli->connect_errno) {
        die();
    }
    
    function login($username, $password) {
        
        global $mysqli;
        
        $query = sprintf("SELECT * FROM users WHERE username = '%s';", $username);
        $res = $mysqli->query($query);
        
        if ($res->num_rows > 0) {
            $res->data_seek(0);
            $user = $res->fetch_assoc();
            
            if ($user['password'] != md5($password)) {
                return false;
            }
            
            return $user;
            
        }
        
        return false;
        
    }
    
    /**
     * Check user is existed or not
     */
    function isUserExisted($username) {
        
        global $mysqli;
        
        $query = sprintf("SELECT * FROM users WHERE username = '%s';", $username);
        $res = $mysqli->query($query);
        if ($res->num_rows > 0) {
            // user existed
            return true;
        } else {
            // user not existed
            return false;
        }
    }
    
    function createUser($username, $email, $password) {
        
        global $mysqli;
        
        $query = sprintf("INSERT INTO users (username, password, email) VALUES('%s','%s', '%s');", $username, md5($password), $email);
        
        $res = $mysqli->query($query);
        
        if ($res) {
            $query = sprintf("SELECT * FROM users WHERE username = '%s';", $username);
            $result = $mysqli->query($query);
            
            $result->data_seek(0);
            $user = $result->fetch_assoc();

            // return user details
            return $user;
        } else {
            return false;
        }
        
    }
    
    function updateEmail($username, $email) {
        global $mysqli;
        
        $query = sprintf("UPDATE users SET email = '%s' WHERE username = '%s'", $email, $username);
        $res = $mysqli->query($query);
    }
    
    function listTasks($limit, $last_date) {
        global $mysqli;
        
        $query = sprintf("SELECT * FROM tasks WHERE date_created > '%s' LIMIT %s;", $last_date, $limit);
        $res = $mysqli->query($query);
        
        $tasks = array();
        while($row = $res->fetch_assoc()) {
            $tasks[] = $row;
        }
        
        return $tasks;
    }
    
    function createTask($id, $username, $name, $description,
                        $complete, $comment, $photo, $audio) {
        
        global $mysqli;
        
        $date_created = date( 'Y-m-d H:i:s' );
        
        if ($id == 0) {
            $query = sprintf("INSERT INTO tasks (username, name, description, complete, comment, photo, audio, date_created) VALUES('%s','%s', '%s', '%s','%s', '%s', '%s','%s');", $username, $name, $description, $complete, $comment, $photo, $audio, $date_created);
            
            $res = $mysqli->query($query);
            
            if ($res) {
                return $mysqli->insert_id;
            } else {
                return false;
            }
        } else {
            $query = sprintf("UPDATE tasks SET name = '%s', description = '%s', complete = '%s', comment = '%s', photo = '%s', audio = '%s' WHERE id = '%s';", $name, $description, $complete, $comment, $photo, $audio, $id);
            
            $res = $mysqli->query($query);
            
            if ($res) {
                return $id;
            } else {
                return false;
            }
        }
        
        return false;

    }
    
    function listComments($taskid) {
        global $mysqli;
        
        $query = sprintf("SELECT * FROM comments WHERE taskid = '%s';", $taskid);
        $res = $mysqli->query($query);
        
        $comments = array();
        while($row = $res->fetch_assoc()) {
            $comments[] = $row;
        }
        
        return $comments;
    }
    
    function createComment($taskid, $username, $comment, $date_created) {
        
        global $mysqli;
        
        $date_created = date( 'Y-m-d H:i:s' );
        
        $query = sprintf("INSERT INTO comments (taskid, username, comment, date_created) VALUES('%s','%s', '%s', '%s');", $taskid, $username, $comment, $date_created);
        
        $res = $mysqli->query($query);
        
        if ($res) {
            return $mysqli->insert_id;
        } else {
            return false;
        }
        
        return false;
        
    }
    
    function listPhotos($taskid) {
        global $mysqli;
        
        $query = sprintf("SELECT * FROM photos WHERE taskid = '%s';", $taskid);
        $res = $mysqli->query($query);
        
        $comments = array();
        while($row = $res->fetch_assoc()) {
            $comments[] = $row;
        }
        
        return $comments;
    }
    
    function createPhoto($taskid, $username, $photo, $date_created) {
        
        global $mysqli;
        
        $date_created = date( 'Y-m-d H:i:s' );
        
        $query = sprintf("INSERT INTO photos (taskid, username, photo, date_created) VALUES('%s','%s', '%s', '%s');", $taskid, $username, $photo, $date_created);
        
        $res = $mysqli->query($query);
        
        if ($res) {
            return $mysqli->insert_id;
        } else {
            return false;
        }
        
        return false;
        
    }


    
    ?>