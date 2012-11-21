<?PHP
    
    
    if (isset($_POST['action']) && $_POST['action'] != '') {
        
        require_once("database_functions.php");
        
        $action = $_POST['action'];
        
        // response Array
        $response = array("action" => $action, "success" => 0, "error" => 0);
        
        // check for tag type
        if ($action == 'login') {
            // Request type is check Login
            $username = $_POST['username'];
            $password = $_POST['password'];
            
            // check for user
            $user = login($username, $password);
            if ($user != false) {
                // user found
                // echo json with success = 1
                $response["success"] = 1;
                $response["uid"] = $user["id"];
                $response["user"]["username"] = $user["username"];
                $response["user"]["email"] = $user["email"];
                echo json_encode($response);
            } else {
                // user not found
                // echo json with error = 1
                $response["error"] = 1;
                $response["error_msg"] = "Incorrect email or password!";
                echo json_encode($response);
            }
        } else if ($action == 'register') {
            
            // Request type is Register new user
            $username = $_POST['username'];
            $email = $_POST['email'];
            $password = $_POST['password'];
            
            // check if user is already existed
            if (isUserExisted($username)) {
                // user is already existed - error response
                $response["error"] = 2;
                $response["error_msg"] = "User already existed";
                echo json_encode($response);
            } else {
                // store user
                $user = createUser($username, $email, $password);
                if ($user) {
                    // user stored successfully
                    $response["success"] = 1;
                    $response["uid"] = $user["id"];
                    $response["user"]["username"] = $user["username"];
                    $response["user"]["email"] = $user["email"];
                    echo json_encode($response);
                } else {
                    // user failed to store
                    $response["error"] = 1;
                    $response["error_msg"] = "Error occured in Registartion";
                    echo json_encode($response);
                }
            }
            
        } else if ($action == 'checkUser') {
            
        } else {
            echo "Invalid Request";
        }
    } else {
        echo "Access Denied";
    }
    
    ?>