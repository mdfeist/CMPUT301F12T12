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
                $response["error_msg"] = "Incorrect username or password";
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
            
        } else if ($action == 'update_email') {
            // Request type is Register new user
            $username = $_POST['username'];
            $email = $_POST['email'];
            
            updateEmail($username, $email);
            
        } else if ($action == 'list_tasks_attachments') {
            $taskid = $_POST['taskid'];
            
            $response["success"] = 1;
            $response["comments"] = getNumberOfComments($taskid);
            $response["photos"] = getNumberOfPhotos($taskid);
            $response["audios"] = getNumberOfAudios($taskid);
            echo json_encode($response);
            
        } else if ($action == 'list_tasks') {
            $limit = $_POST['limit'];
            $last_date = $_POST['date'];
            
            $tasks = listTasks($limit, $last_date);
            
            $response["success"] = 1;
            $response["tasks"] = $tasks;
            echo json_encode($response);
            
        } else if ($action == 'sync_task') {
            
            $u_id = $_POST['id'];
            $username = $_POST['username'];
            $name = $_POST['name'];
            $description = $_POST['description'];
            $complete = $_POST['complete'];
            $comment = $_POST['comment'];
            $photo = $_POST['photo'];
            $audio = $_POST['audio'];
            $date_created = $_POST['date_created'];
            $date_completed = $_POST['date_completed'];
            
            $id = createTask($u_id, $username, $name, $description,
                               $complete, $comment, $photo, $audio,
                               $date_created, $date_completed);
            
            if ($id) {
                $response["success"] = 1;
                $response["id"] = $id;
                echo json_encode($response);
            } else {
                $response["error"] = 1;
                $response["error_msg"] = "Unable to create task";
                echo json_encode($response);
            }
        } else if ($action == 'list_comments') {
            $taskid = $_POST['taskid'];

            $comments = listComments($taskid);
            
            $response["success"] = 1;
            $response["comments"] = $comments;
            echo json_encode($response);
            
        } else if ($action == 'sync_comment') {
            
            $taskid = $_POST['taskid'];
            $username = $_POST['username'];
            $comment = $_POST['content'];
            $date_created = $_POST['date_created'];

            
            $id = createComment($taskid, $username, $comment, $date_created);
            
            if ($id) {
                $response["success"] = 1;
                $response["id"] = $id;
                echo json_encode($response);
            } else {
                $response["error"] = 1;
                $response["error_msg"] = "Unable to create task";
                echo json_encode($response);
            }
        } else if ($action == 'list_photos') {
            $taskid = $_POST['taskid'];
            
            $photos = listPhotos($taskid);
            
            $response["success"] = 1;
            $response["photos"] = $photos;
            echo json_encode($response);
            
        } else if ($action == 'sync_photo') {
            
            $taskid = $_POST['taskid'];
            $username = $_POST['username'];
            $photo= $_POST['content'];
            $date_created = $_POST['date_created'];
            
            
            $id = createPhoto($taskid, $username, $photo, $date_created);
            
            if ($id) {
                $response["success"] = 1;
                $response["id"] = $id;
                echo json_encode($response);
            } else {
                $response["error"] = 1;
                $response["error_msg"] = "Unable to create task";
                echo json_encode($response);
            }
        } else if ($action == 'complete_task') {
            $taskid = $_POST['taskid'];
            $from = $_POST['from'];
            $to = $_POST['to'];
            $message = $_POST['message'];
            
            completeTask($taskid, $from, $to, $message);
            
            $response["success"] = 1;
            echo json_encode($response);
        } else if ($action == 'list_notifications') {
            $username = $_POST['username'];
            
            $notifications = listNotificationsFor($username);
            
            $response["success"] = 1;
            $response["notifications"] = $notifications;
            echo json_encode($response);
            
        } else if ($action == 'delete_notification') {
            $id = $_POST['id'];
            
            deleteNotification($id);
            
            $response["success"] = 1;
            echo json_encode($response);
            
        } else {
            echo "Invalid Request";
        }
    } else {
        echo "Access Denied";
    }
    
    ?>