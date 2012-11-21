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

    
    ?>