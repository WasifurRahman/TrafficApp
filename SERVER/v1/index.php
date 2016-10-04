<?php
 
require_once '../include/DbHandler.php';
require_once '../include/PassHash.php';
require '.././libs/Slim/Slim.php';

set_time_limit(30);
 
\Slim\Slim::registerAutoloader();
 
$app = new \Slim\Slim();

function verifyRequiredParams($required_fields) {
    $error = false;
    $error_fields = "";
    $request_params = array();
    $request_params = $_REQUEST;
    // Handling PUT request params
    if ($_SERVER['REQUEST_METHOD'] == 'PUT') {
        $app = \Slim\Slim::getInstance();
        parse_str($app->request()->getBody(), $request_params);
    }
    foreach ($required_fields as $field) {
        if (!isset($request_params[$field]) || strlen(trim($request_params[$field])) <= 0) {
            $error = true;
            $error_fields .= $field . ', ';
        }
    }
 
    if ($error) {
        // Required field(s) are missing or empty
        // echo error json and stop the app
        $response = array();
        $app = \Slim\Slim::getInstance();
        $response["error"] = true;
        $response["message"] = 'Required field(s) ' . substr($error_fields, 0, -2) . ' is missing or empty';
        echoResponse(400, $response);
        $app->stop();
    }
}

function validateEmail($email) {
    $app = \Slim\Slim::getInstance();
    if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
        $response["error"] = true;
        $response["message"] = 'Email address is not valid';
        echoResponse(400, $response);
        $app->stop();
    }
}

function echoResponse($status_code, $response) {
    $app = \Slim\Slim::getInstance();
    // Http response code
    $app->status($status_code);
 
    // setting response content type to json
    $app->contentType('application/json');
 
//    echo $response;
    echo json_encode($response);
}


//$app->get('/test2', function() {
//    echo 'testing inside test2';
//});
//
//echo "please!";


$app->post('/register', function() use ($app) {
//    echo "testing";
    // check for required params
//    verifyRequiredParams(array('username', 'gender', 'email', 'password'));

    $response = array();
    

    // reading post params
    $username = $app->request->post('username');
    $gender = $app->request->post('gender');
    $email = $app->request->post('email');
    $password = $app->request->post('password');

    // validating email address
//    validateEmail($email);

    $db = new DbHandler();
    $res = $db->createUser($username, $gender, $email, $password);

    if ($res == USER_CREATED_SUCCESSFULLY) {
        
        $user = $db->getUserByEmail($email);

        if ($user != NULL) {
            $response["error"] = false;
            $response['username'] = $user['username'];
            $response['id'] = $user['id'];
            $response['apiKey'] = $user['api_key'];
            $response['message'] = "User successfully registered.";
        } else {
            // unknown error occurred
            $response['error'] = true;
            $response['message'] = "An error occurred. Please try again";
        }
        echoResponse(201, $response);
    } else if ($res == USER_CREATE_FAILED) {
        $response["error"] = true;
        $response["message"] = "Oops! An error occurred while registering";
        echoResponse(200, $response);
    } else if ($res == USER_ALREADY_EXISTED) {
        $response["error"] = true;
        $response["message"] = "Sorry, this email already exists";
        echoResponse(200, $response);
    }
});

$app->post('/login', function() use ($app) {
    // check for required params
    //            verifyRequiredParams(array('email', 'password'));

    // reading post params
    $email = $app->request()->post('email');
    $password = $app->request()->post('password');
    $response = array();

    $db = new DbHandler();
    // check for correct email and password
    if ($db->checkLogin($email, $password)) {
        // get the user by email
        $user = $db->getUserByEmail($email);

        if ($user != NULL) {
            $response["error"] = false;
            $response['username'] = $user['username'];
            $response['id'] = $user['id'];
            $response['apiKey'] = $user['api_key'];
            $response['message'] = "User successfully logged in.";
        } else {
            // unknown error occurred
            $response['error'] = true;
            $response['message'] = "An error occurred. Please try again";
        }
    } else {
        // user credentials are wrong
        $response['error'] = true;
        $response['message'] = 'Login failed. Incorrect credentials';
    }

    echoResponse(200, $response);
    
});

/*
 * UPDATE RELATED METHODS
 */

$app->post('/addupdate', function() use ($app) {
    
    $fromLocationId = $app->request()->post('fromLocationId');
    $toLocationId = $app->request()->post('toLocationId');
    $estTimeToCross = $app->request()->post('estTimeToCross');
    $situation = $app->request()->post('situation');
    $description = $app->request()->post('description');
    $timeOfSituation = $app->request()->post('timeOfSituation');
    $updaterId = $app->request()->post('updaterId');
    $requestId = $app->request()->post('requestId');
    $response = array();

    $db = new DbHandler();
    // check for correct email and password
    if ($db->addUpdate($fromLocationId, $toLocationId, $estTimeToCross, $situation, $description, $timeOfSituation, $updaterId, $requestId)) {
        // get the user by email
        $user = $db->getUserById($updaterId);

        if ($user != NULL) {
            $response["error"] = false;
//            $response['message'] = "New Update Added for the route $location1-$location2";
            $response['message'] = "New Update Added";
        } else {
            // unknown error occurred
            $response['error'] = true;
            $response['message'] = "An error occurred. Please try again.";
        }
    } else {
        // user credentials are wrong
        $response['error'] = true;
        $response['message'] = 'One or more field(s) missing or invalid';
    }

    echoResponse(200, $response);
});



$app->get('/allupdates', function() use ($app){
    $response = array();
    $sortType = $app->request()->get('sortType');
    
    $db = new DbHandler();

    // fetching all user tasks
    $result = $db->getAllUpdates($sortType);
    
    if($result) {
        $response["error"] = false;
        $response["updates"] = array();
//        $response["updates"]["likers"] = array();
//        $response["updates"]["dislikers"] = array();
        
//        $update = array();
//        $update["info"] = array();
//        $update["likers"] = array();
//        $update["dislikers"] = array();
        
        // looping through result and preparing updates array
        while ($update = $result->fetch_assoc()) {
            $tmp = array();
            
            $commentCount = $db->getCommentCount("update", $update["id"]);
            
            $tmp["id"] = $update["id"];
            $tmp["locationIdFrom"] = $update["locationIdFrom"];
            $tmp["locationIdTo"] = $update["locationIdTo"];
            $tmp["estTimeToCross"] = $update["estTimeToCross"];
            $tmp["situation"] = $update["situation"];
            $tmp["description"] = $update["description"];
            $tmp["timeOfSituation"] = $update["timeOfSituation"];
            $tmp["updaterId"] = $update["updaterId"];
            $tmp["updaterName"] = $update["username"];
            $tmp["likeCount"] = $update["likeCount"];
            $tmp["dislikeCount"] = $update["dislikeCount"];
            $tmp["commentCount"] = $commentCount;
            $tmp["requestId"] = $update["requestId"];
            $tmp["timeOfUpdate"] = $update["timeOfUpdate"];
            
//            $tmp3["dislikerName"] = $update["dislikers"]["dislikerName"];
//            $tmp3["dislikerId"] = $update["dislikers"]["dislikerId"];
            
            array_push($response["updates"], $tmp);
//            array_push($response["updates"]["dislikers"], $tmp3);
            
            $likers = $db->getUpdateLikers($update);
        
            while($liker = $likers->fetch_assoc()) {
                $tmp2 = array();
                $tmp2["likerName"] = $liker["likerName"];
                $tmp2["likerId"] = $liker["likerId"];

//                array_push($response["updates"]["likers"], $tmp2);
                array_push($response["updates"], $tmp2);
            }
            
            $dislikers = $db->getUpdateDislikers($update);
        
            while($disliker = $dislikers->fetch_assoc()) {
                $tmp3 = array();
                $tmp3["dislikerName"] = $disliker["dislikerName"];
                $tmp3["dislikerId"] = $disliker["dislikerId"];

//                array_push($response["updates"]["likers"], $tmp2);
                array_push($response["updates"], $tmp3);
            }
        }
        
//        $likers = $db->getLikers($result);
//        
//        while($liker = $likers->fetch_assoc()) {
//            $tmp2 = array();
//            $tmp2["likerName"] = $liker["likerName"];
//            $tmp2["likerId"] = $liker["likerId"];
//            
//            array_push($response["updates"]["likers"], $tmp2);
//        }
        
        
        $response["message"] = "Query Successful";
    }
    else {
        // user credentials are wrong
        $response['error'] = true;
        $response['message'] = 'One or more field(s) missing or invalid';
    }
    
    echoResponse(200, $response);
});


$app->post('/addupdatelike', function() use ($app) {
    $response = array();
    $likerId = $app->request()->post('likerId');
    $updateId = $app->request()->post('updateId');
    
    $db = new DbHandler();
    
    // fetching all user tasks
    $result = $db->addUpdateLike($updateId, $likerId);

    if($result) {
        $response["error"] = false;
        $response["message"] = "Query Successful";
    }
    else {
        // user credentials are wrong
        $response['error'] = true;
        $response['message'] = "The user vote could not be added to the database";
    }
    echoResponse(200, $response);
});

$app->post('/addupdatecomment', function() use ($app) {
    $response = array();
    $commenterId = $app->request()->post('commenterId');
    $updateId = $app->request()->post('updateId');
    $commentText = $app->request()->post('commentText');
    
    $db = new DbHandler();
    
    // fetching all user tasks
    $result = $db->addUpdateComment($updateId, $commenterId, $commentText);

    if($result) {
        $response["error"] = false;
        $response["message"] = "Query Successful";
    }
    else {
        // user credentials are wrong
        $response['error'] = true;
        $response['message'] = "The user comment could not be added to the database";
    }
    echoResponse(200, $response);
});

$app->post('/removeupdatelike', function() use ($app) {
    $response = array();
    $likerId = $app->request()->post('likerId');
    $updateId = $app->request()->post('updateId');
    
    $db = new DbHandler();
    
    // fetching all user tasks
    $result = $db->removeUpdateLike($updateId, $likerId);

    if($result) {
        $response["error"] = false;
        $response["message"] = "Query Successful";
    }
    else {
        // user credentials are wrong
        $response['error'] = true;
        $response['message'] = "The user vote could not be added to the database";
    }
    echoResponse(200, $response);
});

$app->post('/addupdatedislike', function() use ($app) {
    $response = array();
    $dislikerId = $app->request()->post('dislikerId');
    $updateId = $app->request()->post('updateId');
    
    $db = new DbHandler();
    
    // fetching all user tasks
    $result = $db->addUpdateDislike($updateId, $dislikerId);

    if($result) {
        $response["error"] = false;
        $response["message"] = "Query Successful";
    }
    else {
        // user credentials are wrong
        $response['error'] = true;
        $response['message'] = "The user vote could not be added to the database";
    }
    echoResponse(200, $response);
});

$app->post('/removeupdatedislike', function() use ($app) {
    $response = array();
    $dislikerId = $app->request()->post('dislikerId');
    $updateId = $app->request()->post('updateId');
    
    $db = new DbHandler();
    
    // fetching all user tasks
    $result = $db->removeUpdateDislike($updateId, $dislikerId);

    if($result) {
        $response["error"] = false;
        $response["message"] = "Query Successful";
    }
    else {
        // user credentials are wrong
        $response['error'] = true;
        $response['message'] = "The user vote could not be added to the database";
    }
    echoResponse(200, $response);
});

$app->get('/updatesfromlocation', function() use ($app){
    $response = array();
    $sortType = $app->request()->get('sortType');
    $locationId = $app->request()->get('locationId');
    
    $db = new DbHandler();

    // fetching all user tasks
    $result = $db->getUpdatesFromLocation($sortType, $locationId);

    if($result) {
        $response["error"] = false;
        $response["updates"] = array();

        // looping through result and preparing updates array
        while ($update = $result->fetch_assoc()) {
            $tmp = array();
            $tmp["id"] = $update["id"];
            $tmp["locationIdFrom"] = $update["locationIdFrom"];
            $tmp["locationIdTo"] = $update["locationIdTo"];
            $tmp["estTimeToCross"] = $update["estTimeToCross"];
            $tmp["situation"] = $update["situation"];
            $tmp["description"] = $update["description"];
            $tmp["timeOfSituation"] = $update["timeOfSituation"];
            $tmp["updaterId"] = $update["updaterId"];
            $tmp["updaterName"] = $update["username"];
            $tmp["likeCount"] = $update["likeCount"];
            $tmp["dislikeCount"] = $update["dislikeCount"];
            $tmp["requestId"] = $update["requestId"];
            $tmp["timeOfUpdate"] = $update["timeOfUpdate"];
            array_push($response["updates"], $tmp);
            
            
            $likers = $db->getUpdateLikers($update);
        
            while($liker = $likers->fetch_assoc()) {
                $tmp2 = array();
                $tmp2["likerName"] = $liker["likerName"];
                $tmp2["likerId"] = $liker["likerId"];

//                array_push($response["updates"]["likers"], $tmp2);
                array_push($response["updates"], $tmp2);
            }
            
            $dislikers = $db->getUpdateDislikers($update);
        
            while($disliker = $dislikers->fetch_assoc()) {
                $tmp3 = array();
                $tmp3["dislikerName"] = $disliker["dislikerName"];
                $tmp3["dislikerId"] = $disliker["dislikerId"];

//                array_push($response["updates"]["likers"], $tmp2);
                array_push($response["updates"], $tmp3);
            }
        }
        $response["message"] = "Query Successful";
    }
    else {
        // user credentials are wrong
        $response['error'] = true;
        $response['message'] = 'One or more field(s) missing or invalid';
    }

    echoResponse(200, $response);
});       

$app->get('/getupdatebyid', function() use ($app){
    $response = array();
    $updateId = $app->request()->get('updateId');
    
    $db = new DbHandler();

    // fetching all user tasks
    $result = $db->getUpdateById($updateId);

    if($result) {
        $response["error"] = false;
        $response["updates"] = array();
//        $response["comments"] = array();

        // looping through result and preparing updates array
//        while ($update = $result->fetch_assoc()) {
        $update = $result->fetch_assoc();
        $tmp = array();
        $tmp["id"] = $update["id"];
        $tmp["locationIdFrom"] = $update["locationIdFrom"];
        $tmp["locationIdTo"] = $update["locationIdTo"];
        $tmp["estTimeToCross"] = $update["estTimeToCross"];
        $tmp["situation"] = $update["situation"];
        $tmp["description"] = $update["description"];
        $tmp["timeOfSituation"] = $update["timeOfSituation"];
        $tmp["updaterId"] = $update["updaterId"];
        $tmp["updaterName"] = $update["username"];
        $tmp["likeCount"] = $update["likeCount"];
        $tmp["dislikeCount"] = $update["dislikeCount"];
        $tmp["requestId"] = $update["requestId"];
        $tmp["timeOfUpdate"] = $update["timeOfUpdate"];
        array_push($response["updates"], $tmp);


        $likers = $db->getUpdateLikers($update);

        while($liker = $likers->fetch_assoc()) {
            $tmp2 = array();
            $tmp2["likerName"] = $liker["likerName"];
            $tmp2["likerId"] = $liker["likerId"];

//                array_push($response["updates"]["likers"], $tmp2);
            array_push($response["updates"], $tmp2);
        }

        $dislikers = $db->getUpdateDislikers($update);

        while($disliker = $dislikers->fetch_assoc()) {
            $tmp3 = array();
            $tmp3["dislikerName"] = $disliker["dislikerName"];
            $tmp3["dislikerId"] = $disliker["dislikerId"];

//                array_push($response["updates"]["likers"], $tmp2);
            array_push($response["updates"], $tmp3);
        }
            
       
//        $comments = $db->getUpdateComments($updateId);
//        while ($comment = $comments->fetch_assoc()) {
//            $tmp = array();
//            $tmp["commentId"] = $comment["id"];
//            $tmp["updateId"] = $comment["updateId"];
//            $tmp["commenterId"] = $comment["commenterId"];
//            $tmp["commenterName"] = $comment["commenterName"];
//            $tmp["commentText"] = $comment["commentText"];
//            $tmp["timeOfComment"] = $comment["timeOfComment"];
//            array_push($response["comments"], $tmp);
//        }
        
        $response["message"] = "Query Successful";
    }
    else {
        // user credentials are wrong
        $response['error'] = true;
        $response['message'] = 'One or more field(s) missing or invalid';
    }

    echoResponse(200, $response);
});


$app->get('/userupdates', function() use ($app){
    $response = array();
    $sortType = $app->request()->get('sortType');
    $userId = $app->request()->get('userId');
    
    $db = new DbHandler();

    // fetching all user tasks
    $result = $db->getUserUpdates($sortType, $userId);

    if($result) {
        $response["error"] = false;
        $response["updates"] = array();

        // looping through result and preparing updates array
        while ($update = $result->fetch_assoc()) {
            $tmp = array();
            $tmp["id"] = $update["id"];
            $tmp["locationIdFrom"] = $update["locationIdFrom"];
            $tmp["locationIdTo"] = $update["locationIdTo"];
            $tmp["estTimeToCross"] = $update["estTimeToCross"];
            $tmp["situation"] = $update["situation"];
            $tmp["description"] = $update["description"];
            $tmp["timeOfSituation"] = $update["timeOfSituation"];
            $tmp["updaterId"] = $update["updaterId"];
            $tmp["likeCount"] = $update["likeCount"];
            $tmp["dislikeCount"] = $update["dislikeCount"];
            $tmp["requestId"] = $update["requestId"];
            $tmp["timeOfUpdate"] = $update["timeOfUpdate"];
            array_push($response["updates"], $tmp);
            
            $result = $db->getUserUpdates($sortType, $userId);
        }
        $response["message"] = "Query Successful";
    }
    
    else {
        // user credentials are wrong
        $response['error'] = true;
        $response['message'] = 'One or more field(s) missing or invalid';
    }

    echoResponse(200, $response);
});

$app->get('/updatecomments', function() use ($app){
    $response = array();
    $updateId = $app->request()->get('updateId');
    
    $db = new DbHandler();

    // fetching all user tasks
    $result = $db->getUpdateComments($updateId);
    
    $response["error"] = false;
//        $commentCount = $db->getCommentCount("update", $updateId);
//        echo $commentCount;
//        $response["commentCount"] = $commentCount["count"];
    $response["comments"] = array();

    // looping through result and preparing updates array
    while ($comment = $result->fetch_assoc()) {
        $tmp = array();
        $tmp["commentId"] = $comment["id"];
        $tmp["updateId"] = $comment["updateId"];
        $tmp["commenterId"] = $comment["commenterId"];
        $tmp["commenterName"] = $comment["commenterName"];
        $tmp["commentText"] = $comment["commentText"];
        $tmp["timeOfComment"] = $comment["timeOfComment"];
        array_push($response["comments"], $tmp);

    }

    $response["message"] = "Query Successful";
    
    echoResponse(200, $response);
});       


/*
 * POST RELATED METHODS
 */

$app->post('/addpost', function() use ($app) {
    
    $postType = $app->request()->post('postType');
    $locationId = $app->request()->post('locationId');
    $description = $app->request()->post('description');
    $posterId = $app->request()->post('posterId');
    $title = $app->request()->post('title');
    $source = $app->request()->post('source');
    $response = array();

    $db = new DbHandler();
    // check for correct email and password
    if ($db->addPost($postType, $locationId, $description, $posterId, $title, $source)) {
        // get the user by email
        $user = $db->getUserById($posterId);
        
        if ($user != NULL) {
            $response["error"] = false;
            $response['username'] = $user['username'];
            $response['id'] = $user['id'];
            $response['apiKey'] = $user['api_key'];
//            $response['message'] = "New Update Added for the route $location1-$location2";
            if($postType == "discussion")
                $response['message'] = "New Discussion Added";
            else
                $response['message'] = "New Announcement Added";
        } else {
            // unknown error occurred
            $response['error'] = true;
            $response['message'] = "An error occurred. Please try again.";
        }
    } else {
        // user credentials are wrong
        $response['error'] = true;
        $response['message'] = 'One or more field(s) missing or invalid';
    }

    echoResponse(200, $response);
});

$app->post('/addpostcomment', function() use ($app) {
    $response = array();
    $commenterId = $app->request()->post('commenterId');
    $postId = $app->request()->post('postId');
    $commentText = $app->request()->post('commentText');
    
    $db = new DbHandler();
    
    // fetching all user tasks
    $result = $db->addPostComment($postId, $commenterId, $commentText);

    if($result) {
        $response["error"] = false;
        $response["message"] = "Query Successful";
    }
    else {
        // user credentials are wrong
        $response['error'] = true;
        $response['message'] = "The user comment could not be added to the database";
    }
    echoResponse(200, $response);
});

$app->get('/allposts', function() use ($app){
    $response = array();
    $sortType = $app->request()->get('sortType');
    $postType = $app->request()->get('postType');
    
    $db = new DbHandler();

    // fetching all user tasks
    $result = $db->getAllPosts($sortType, $postType);

    $response["error"] = false;

    // looping through result and preparing updates array
    if($result) {
        $response["error"] = false;
        $response["posts"] = array();
        $post = $result->fetch_assoc();

        if($post == NULL) {
            $response["message"] = "No Posts Found";
            echoResponse(200, $response);
            return;
        }


        // looping through result and preparing updates array
        do {
            $tmp = array();
            
            $commentCount = $db->getCommentCount("post", $post["id"]);
            
            
            $tmp["id"] = $post["id"];
            $tmp["locationId"] = $post["locationId"];
            $tmp["posterId"] = $post["posterId"];
            $tmp["posterName"] = $post["username"];
            $tmp["postType"] = $post["type"];
            $tmp["description"] = $post["description"];
            $tmp["likeCount"] = $post["likeCount"];
            $tmp["dislikeCount"] = $post["dislikeCount"];
            $tmp["commentCount"] = $commentCount;
            $tmp["title"] = $post["title"];
            $tmp["source"] = $post["source"];
            $tmp["timeOfPost"] = $post["timeOfPost"];

            array_push($response["posts"], $tmp);
            
            $likers = $db->getPostLikers($post);
        
            while($liker = $likers->fetch_assoc()) {
                $tmp2 = array();
                $tmp2["likerName"] = $liker["likerName"];
                $tmp2["likerId"] = $liker["likerId"];

//                array_push($response["updates"]["likers"], $tmp2);
                array_push($response["posts"], $tmp2);
            }
            
            $dislikers = $db->getPostDislikers($post);
        
            while($disliker = $dislikers->fetch_assoc()) {
                $tmp3 = array();
                $tmp3["dislikerName"] = $disliker["dislikerName"];
                $tmp3["dislikerId"] = $disliker["dislikerId"];

//                array_push($response["updates"]["likers"], $tmp2);
                array_push($response["posts"], $tmp3);
            }
        } while ($post  = $result->fetch_assoc());
        
        $response["message"] = "Query Successful";
    } 
    else {
        $response["error"] = true;
        $response["message"] = "One or more field(s) missing or invalid.";
    }

    echoResponse(200, $response);
});


$app->get('/getpostbyid', function() use ($app){
    $response = array();
    $postId = $app->request()->get('postId');
    
    $db = new DbHandler();

    // fetching all user tasks
    $result = $db->getPostById($postId);

    if($result) {
        $response["error"] = false;
        $response["posts"] = array();
        $response["comments"] = array();

        // looping through result and preparing updates array
//        while ($update = $result->fetch_assoc()) {
        $post = $result->fetch_assoc();
        $tmp = array();
        $tmp["id"] = $post["id"];
        $tmp["locationId"] = $post["locationId"];
        $tmp["posterId"] = $post["posterId"];
        $tmp["posterName"] = $post["username"];
        $tmp["postType"] = $post["type"];
        $tmp["description"] = $post["description"];
        $tmp["likeCount"] = $post["likeCount"];
        $tmp["dislikeCount"] = $post["dislikeCount"];
        $tmp["title"] = $post["title"];
        $tmp["source"] = $post["source"];
        $tmp["timeOfPost"] = $post["timeOfPost"];

        array_push($response["posts"], $tmp);


        $likers = $db->getPostLikers($post);

        while($liker = $likers->fetch_assoc()) {
            $tmp2 = array();
            $tmp2["likerName"] = $liker["likerName"];
            $tmp2["likerId"] = $liker["likerId"];

//                array_push($response["updates"]["likers"], $tmp2);
            array_push($response["posts"], $tmp2);
        }

        $dislikers = $db->getPostDislikers($post);

        while($disliker = $dislikers->fetch_assoc()) {
            $tmp3 = array();
            $tmp3["dislikerName"] = $disliker["dislikerName"];
            $tmp3["dislikerId"] = $disliker["dislikerId"];

//                array_push($response["updates"]["likers"], $tmp2);
            array_push($response["posts"], $tmp3);
        }
            
       
        $comments = $db->getPostComments($postId);
        while ($comment = $comments->fetch_assoc()) {
            $tmp = array();
            $tmp["commentId"] = $comment["id"];
            $tmp["postId"] = $comment["postId"];
            $tmp["commenterId"] = $comment["commenterId"];
            $tmp["commenterName"] = $comment["commenterName"];
            $tmp["commentText"] = $comment["commentText"];
            $tmp["timeOfComment"] = $comment["timeOfComment"];
            array_push($response["comments"], $tmp);
        }
        
        $response["message"] = "Query Successful";
    }
    else {
        // user credentials are wrong
        $response['error'] = true;
        $response['message'] = 'One or more field(s) missing or invalid';
    }

    echoResponse(200, $response);
});

$app->post('/addpostcomment', function() use ($app) {
    $response = array();
    $commenterId = $app->request()->post('commenterId');
    $postId = $app->request()->post('postId');
    $commentText = $app->request()->post('commentText');
    
    $db = new DbHandler();
    
    // fetching all user tasks
    $result = $db->addPostComment($postId, $commenterId, $commentText);

    if($result) {
        $response["error"] = false;
        $response["message"] = "Query Successful";
    }
    else {
        // user credentials are wrong
        $response['error'] = true;
        $response['message'] = "The user comment could not be added to the database";
    }
    echoResponse(200, $response);
});

$app->get('/postcomments', function() use ($app){
    $response = array();
    $postId = $app->request()->get('postId');
    
    $db = new DbHandler();

    // fetching all user tasks
    $result = $db->getPostComments($postId);
    
    $response["error"] = false;
//        $commentCount = $db->getCommentCount("update", $updateId);
//        echo $commentCount;
//        $response["commentCount"] = $commentCount["count"];
    $response["comments"] = array();

    // looping through result and preparing updates array
    while ($comment = $result->fetch_assoc()) {
        $tmp = array();
        $tmp["commentId"] = $comment["id"];
        $tmp["postId"] = $comment["postId"];
        $tmp["commenterId"] = $comment["commenterId"];
        $tmp["commenterName"] = $comment["commenterName"];
        $tmp["commentText"] = $comment["commentText"];
        $tmp["timeOfComment"] = $comment["timeOfComment"];
        array_push($response["comments"], $tmp);

    }

    $response["message"] = "Query Successful";
    
    echoResponse(200, $response);
});

$app->post('/addpostlike', function() use ($app) {
    $response = array();
    $likerId = $app->request()->post('likerId');
    $postId = $app->request()->post('postId');
    
    $db = new DbHandler();
    
    // fetching all user tasks
    $result = $db->addPostLike($postId, $likerId);

    if($result) {
        $response["error"] = false;
        $response["message"] = "Query Successful";
    }
    else {
        // user credentials are wrong
        $response['error'] = true;
        $response['message'] = "The user vote could not be added to the database";
    }
    echoResponse(200, $response);
});

$app->post('/removepostlike', function() use ($app) {
    $response = array();
    $likerId = $app->request()->post('likerId');
    $postId = $app->request()->post('postId');
    
    $db = new DbHandler();
    
    // fetching all user tasks
    $result = $db->removePostLike($postId, $likerId);

    if($result) {
        $response["error"] = false;
        $response["message"] = "Query Successful";
    }
    else {
        // user credentials are wrong
        $response['error'] = true;
        $response['message'] = "The user vote could not be added to the database";
    }
    echoResponse(200, $response);
});

$app->post('/addpostdislike', function() use ($app) {
    $response = array();
    $dislikerId = $app->request()->post('dislikerId');
    $postId = $app->request()->post('postId');
    
    $db = new DbHandler();
    
    // fetching all user tasks
    $result = $db->addPostDislike($postId, $dislikerId);

    if($result) {
        $response["error"] = false;
        $response["message"] = "Query Successful";
    }
    else {
        // user credentials are wrong
        $response['error'] = true;
        $response['message'] = "The user vote could not be added to the database";
    }
    echoResponse(200, $response);
});

$app->post('/removepostdislike', function() use ($app) {
    $response = array();
    $dislikerId = $app->request()->post('dislikerId');
    $postId = $app->request()->post('postId');
    
    $db = new DbHandler();
    
    // fetching all user tasks
    $result = $db->removePostDislike($postId, $dislikerId);

    if($result) {
        $response["error"] = false;
        $response["message"] = "Query Successful";
    }
    else {
        // user credentials are wrong
        $response['error'] = true;
        $response['message'] = "The user vote could not be added to the database";
    }
    echoResponse(200, $response);
});

$app->get('/postsfromlocation', function() use ($app){
    $response = array();
    $sortType = $app->request()->get('sortType');
    $locationId = $app->request()->get('locationId');
    $postType = $app->request()->get('postType');
    
    $db = new DbHandler();

    // fetching all user tasks
    $result = $db->getPostsFromLocation($sortType, $postType, $locationId);

    $response["error"] = false;
    $response["posts"] = array();

    // looping through result and preparing updates array
    if($result) {
        $response["error"] = false;
        $response["posts"] = array();
        $post = $result->fetch_assoc();

        if($post == NULL) {
            $response["message"] = "No Posts Found";
            echoResponse(200, $response);
            return;
        }


        // looping through result and preparing updates array
        while ($post) {
            $tmp = array();
            $tmp["id"] = $post["id"];
            $tmp["locationId"] = $post["locationId"];
            $tmp["posterId"] = $post["posterId"];
            $tmp["posterName"] = $post["username"];
            $tmp["postType"] = $post["type"];
            $tmp["description"] = $post["description"];
            $tmp["likeCount"] = $post["likeCount"];
            $tmp["dislikeCount"] = $post["dislikeCount"];
            $tmp["title"] = $post["title"];
            $tmp["source"] = $post["source"];
            $tmp["timeOfPost"] = $post["timeOfPost"];

            array_push($response["posts"], $tmp);
            $post = $result->fetch_assoc();
        }
        $response["message"] = "Query Successful";
    } 
    else {
        $response["error"] = true;
        $response["message"] = "One or more field(s) missing or invalid.";
    }

    echoResponse(200, $response);
});

$app->get('/postcomments', function() use ($app){
    $response = array();
    $postId = $app->request()->get('postId');
    
    $db = new DbHandler();

    // fetching all user tasks
    $result = $db->getPostComments($postId);
    
    $response["error"] = false;
//        $commentCount = $db->getCommentCount("update", $updateId);
//        echo $commentCount;
//        $response["commentCount"] = $commentCount["count"];
    $response["comments"] = array();

    // looping through result and preparing updates array
    while ($comment = $result->fetch_assoc()) {
        $tmp = array();
        $tmp["commentId"] = $comment["id"];
        $tmp["postId"] = $comment["postId"];
        $tmp["commenterId"] = $comment["commenterId"];
        $tmp["commenterName"] = $comment["commenterName"];
        $tmp["commentText"] = $comment["commentText"];
        $tmp["timeOfComment"] = $comment["timeOfComment"];
        array_push($response["comments"], $tmp);

    }

    $response["message"] = "Query Successful";
    
    echoResponse(200, $response);
});


$app->get('/userposts', function() use ($app){
    $response = array();
    $sortType = $app->request()->get('sortType');
    $postType = $app->request()->get('postType');
    $userId = $app->request()->get('userId');
    
    $db = new DbHandler();
//echo "testing";
    // fetching all user tasks
    $result = $db->getUserPosts($sortType, $postType, $userId);

    // looping through result and preparing updates array
    if($result) {
        $response["error"] = false;
        $response["posts"] = array();
        $post = $result->fetch_assoc();

        if($post == NULL) {
            $response["message"] = "No Posts Found";
            echoResponse(200, $response);
            return;
        }


        // looping through result and preparing updates array
        while ($post) {
            $tmp = array();
            $tmp["id"] = $post["id"];
            $tmp["locationId"] = $post["locationId"];
            $tmp["posterId"] = $post["posterId"];
            $tmp["postType"] = $post["type"];
            $tmp["description"] = $post["description"];
            $tmp["likeCount"] = $post["likeCount"];
            $tmp["dislikeCount"] = $post["dislikeCount"];
            $tmp["title"] = $post["title"];
            $tmp["source"] = $post["source"];
            $tmp["timeOfPost"] = $post["timeOfPost"];

            array_push($response["posts"], $tmp);
            $post = $result->fetch_assoc();
        }
        $response["message"] = "Query Successful";
    } 
    else {
        $response["error"] = true;
        $response["message"] = "One or more field(s) missing or invalid.";
    }

    echoResponse(200, $response);
});


$app->post('/addrequest', function() use ($app) {
    
    $fromLocationId = $app->request()->post('fromLocationId');
    $toLocationId = $app->request()->post('toLocationId');
    $description = $app->request()->post('description');
    $requesterId = $app->request()->post('requesterId');
    $response = array();

    $db = new DbHandler();
    // check for correct email and password
    if ($db->addRequest($fromLocationId, $toLocationId, $description, $requesterId)) {
        // get the user by email
        $user = $db->getUserById($requesterId);
        $location1 = $db->getLocation($fromLocationId);
        $location2 = $db->getLocation($toLocationId);

        if ($user != NULL && $location1 != NULL && $location2 != NULL) {
            $response["error"] = false;
            $response['username'] = $user['username'];
            $response['id'] = $user['id'];
            $response['apiKey'] = $user['api_key'];
//            $response['message'] = "New Update Added for the route $location1-$location2";
            $response['message'] = "New Request Added";
        } else {
            // unknown error occurred
            $response['error'] = true;
            $response['message'] = "An error occurred. Please try again.";
        }
    } else {
        // user credentials are wrong
        $response['error'] = true;
        $response['message'] = 'One or more field(s) missing or invalid';
    }

    echoResponse(200, $response);
});


$app->get('/allrequests', function() use ($app){
    $response = array();
    $sortType = $app->request()->get('sortType');
    
    $db = new DbHandler();

    // fetching all user tasks
    $result = $db->getAllRequests($sortType);

    if($result) {
        $response["error"] = false;
        $response["requests"] = array();
        $request = $result->fetch_assoc();

        if($request == NULL) {
            $response["message"] = "No Requests Found";
            echoResponse(200, $response);
            return;
        }
        // looping through result and preparing updates array
        do {
            $tmp = array();
            $tmp["id"] = $request["id"];
            $tmp["locationIdFrom"] = $request["locationIdFrom"];
            $tmp["locationIdTo"] = $request["locationIdTo"];
            $tmp["description"] = $request["description"];
            $tmp["requesterId"] = $request["requesterId"];
            $tmp["requesterName"] = $request["username"];
            $tmp["followerCount"] = $request["followerCount"];
            $tmp["timeOfRequest"] = $request["timeOfRequest"];
            $tmp["status"] = $request["status"];
            array_push($response["requests"], $tmp);
            
            $followers = $db->getFollowers($request);
        
            while($follower = $followers->fetch_assoc()) {
                $tmp2 = array();
                $tmp2["followerName"] = $follower["followerName"];
                $tmp2["followerId"] = $follower["followerId"];

//                array_push($response["updates"]["likers"], $tmp2);
                array_push($response["requests"], $tmp2);
            }
        }while ($request = $result->fetch_assoc());
        
        $response["message"] = "Query Successful";
    }
    else {
        $response["error"] = true;
        $response["message"] = "One or more field(s) missing or invalid.";
    }

    echoResponse(200, $response);
});

$app->get('/getrequestbyid', function() use ($app){
    $response = array();
    $requestId = $app->request()->get('requestId');
    
    $db = new DbHandler();

    // fetching all user tasks
    $result = $db->getRequestById($requestId);

    if($result) {
        $response["error"] = false;
        $response["requests"] = array();

        // looping through result and preparing updates array
//        while ($update = $result->fetch_assoc()) {
        $request = $result->fetch_assoc();
        $tmp = array();
        $tmp["id"] = $request["id"];
        $tmp["locationIdFrom"] = $request["locationIdFrom"];
        $tmp["locationIdTo"] = $request["locationIdTo"];
        $tmp["description"] = $request["description"];
        $tmp["requesterId"] = $request["requesterId"];
        $tmp["requesterName"] = $request["username"];
        $tmp["followerCount"] = $request["followerCount"];
        $tmp["timeOfRequest"] = $request["timeOfRequest"];
        $tmp["status"] = $request["status"];
        array_push($response["requests"], $tmp);

        $followers = $db->getFollowers($request);

        while($follower = $followers->fetch_assoc()) {
            $tmp2 = array();
            $tmp2["followerName"] = $follower["followerName"];
            $tmp2["followerId"] = $follower["followerId"];

//                array_push($response["updates"]["likers"], $tmp2);
            array_push($response["requests"], $tmp2);
        }
            
        $response["message"] = "Query Successful";
    }
    else {
        // user credentials are wrong
        $response['error'] = true;
        $response['message'] = 'One or more field(s) missing or invalid';
    }

    echoResponse(200, $response);
});

$app->post('/addrequestfollower', function() use ($app) {
    $response = array();
    $followerId = $app->request()->post('followerId');
    $requestId = $app->request()->post('requestId');
    
    $db = new DbHandler();
    
    // fetching all user tasks
    $result = $db->addRequestFollower($requestId, $followerId);

    if($result) {
        $response["error"] = false;
        $response["message"] = "Query Successful";
    }
    else {
        // user credentials are wrong
        $response['error'] = true;
        $response['message'] = "The request follower could not be added to the database";
    }
    echoResponse(200, $response);
});

$app->post('/removerequestfollower', function() use ($app) {
    $response = array();
    $followerId = $app->request()->post('followerId');
    $requestId = $app->request()->post('requestId');
    
    $db = new DbHandler();
    
    // fetching all user tasks
    $result = $db->removeRequestFollower($requestId, $followerId);

    if($result) {
        $response["error"] = false;
        $response["message"] = "Query Successful";
    }
    else {
        // user credentials are wrong
        $response['error'] = true;
        $response['message'] = "The request follower could not be removed from the database";
    }
    echoResponse(200, $response);
});

$app->get('/requestsfromlocation', function() use ($app){
    $response = array();
    $sortType = $app->request()->get('sortType');
    $locationId = $app->request()->get('locationId');
    
    $db = new DbHandler();

    // fetching all user tasks
    $result = $db->getRequestsFromLocation($sortType, $locationId);

    if($result) {
        $response["error"] = false;
        $response["requests"] = array();
        $request = $result->fetch_assoc();

        if($request == NULL) {
            $response["message"] = "No Requests Found";
            echoResponse(200, $response);
            return;
        }
        // looping through result and preparing updates array
        do {
            $tmp = array();
            $tmp["id"] = $request["id"];
            $tmp["locationIdFrom"] = $request["locationIdFrom"];
            $tmp["locationIdTo"] = $request["locationIdTo"];
            $tmp["description"] = $request["description"];
            $tmp["requesterId"] = $request["requesterId"];
            $tmp["requesterName"] = $request["username"];
            $tmp["followerCount"] = $request["followerCount"];
            $tmp["timeOfRequest"] = $request["timeOfRequest"];
            $tmp["status"] = $request["status"];
            array_push($response["requests"], $tmp);
            
            
            $followers = $db->getFollowers($request);
        
            while($follower = $followers->fetch_assoc()) {
                $tmp2 = array();
                $tmp2["followerName"] = $follower["followerName"];
                $tmp2["followerId"] = $follower["followerId"];

//                array_push($response["updates"]["likers"], $tmp2);
                array_push($response["requests"], $tmp2);
            }

        }while ($request = $result->fetch_assoc());
        
        $response["message"] = "Query Successful";
    }
    else {
        $response["error"] = true;
        $response["message"] = "One or more field(s) missing or invalid.";
    }
    echoResponse(200, $response);
});       


$app->get('/userrequests', function() use ($app){
    $response = array();
    $sortType = $app->request()->get('sortType');
    $userId = $app->request()->get('userId');
    
    $db = new DbHandler();
//echo "testing";
    // fetching all user tasks
    $result = $db->getUserRequests($sortType, $userId);

    // looping through result and preparing updates array
    if($result) {
        $response["error"] = false;
        $response["requests"] = array();
        $request = $result->fetch_assoc();

        if($request == NULL) {
            $response["message"] = "No Requests Found";
            echoResponse(200, $response);
            return;
        }
        // looping through result and preparing updates array
        while ($request) {
            $tmp = array();
            $tmp["id"] = $request["id"];
            $tmp["locationIdFrom"] = $request["locationIdFrom"];
            $tmp["locationIdTo"] = $request["locationIdTo"];
            $tmp["description"] = $request["description"];
            $tmp["requesterId"] = $request["requesterId"];
            $tmp["followerCount"] = $request["followerCount"];
            $tmp["timeOfRequest"] = $request["timeOfRequest"];
            $tmp["status"] = $request["status"];
            array_push($response["requests"], $tmp);
            
            $request = $result->fetch_assoc();
        }
        $response["message"] = "Query Successful";
    } 
    else {
        $response["error"] = true;
        $response["message"] = "One or more field(s) missing or invalid.";
    }

    echoResponse(200, $response);
});

/*
 *  LOCATION RELATED METHODS
 * 
 */

$app->get('/locations', function() use ($app) {
    $response = array();
    
    $db = new DbHandler();
    
    $result = $db->getLocations();
    
    if($result) {
        $response["error"] = false;
        $response["locations"] = array();
        $location = $result->fetch_assoc();

        if($location == NULL) {
            $response["message"] = "No Locations Found";
            echoResponse(200, $response);
            return;
        }
        // looping through result and preparing updates array
        while ($location) {
            $tmp = array();
            $tmp["locationId"] = $location["id"];
            $tmp["locationName"] = $location["name"];
            array_push($response["locations"], $tmp);

            $location = $result->fetch_assoc();
        }
        $response["message"] = "Query Successful";
    }
    else {
        $response["error"] = true;
        $response["message"] = "One or more field(s) missing or invalid.";
    }

    echoResponse(200, $response);
});

$app->post('/followlocation', function() use ($app) {
    
    $locationId = $app->request()->post('locationId');
    $followerId = $app->request()->post('followerId');
    $response = array();

    $db = new DbHandler();
    // check for correct email and password
    if ($db->followLocation($locationId, $followerId)) {
        // get the user by email
        $user = $db->getUserById($followerId);
        $location = $db->getLocation($locationId);

        if ($user != NULL && $location != NULL) {
            $response["error"] = false;
//            $response['message'] = "New Update Added for the route $location1-$location2";
            $response['message'] = "New Location Follower Added";
        } else {
            // unknown error occurred
            $response['error'] = true;
            $response['message'] = "An error occurred. Please try again.";
        }
    } else {
        // user credentials are wrong
        $response['error'] = true;
        $response['message'] = 'One or more field(s) missing or invalid';
    }

    echoResponse(200, $response);
});

$app->post('/unfollowlocation', function() use ($app) {
    
    $locationId = $app->request()->post('locationId');
    $followerId = $app->request()->post('followerId');
    $response = array();

    $db = new DbHandler();
    // check for correct email and password
    if ($db->unfollowLocation($locationId, $followerId)) {
        // get the user by email
        $user = $db->getUserById($followerId);
        $location = $db->getLocation($locationId);

        if ($user != NULL && $location != NULL) {
            $response["error"] = false;
//            $response['message'] = "New Update Added for the route $location1-$location2";
            $response['message'] = "Location Follower Removed";
        } else {
            // unknown error occurred
            $response['error'] = true;
            $response['message'] = "An error occurred. Please try again.";
        }
    } else {
        // user credentials are wrong
        $response['error'] = true;
        $response['message'] = 'One or more field(s) missing or invalid';
    }

    echoResponse(200, $response);
});


$app->get('/locationfollowers', function() use ($app){
    $response = array();
    $locationId = $app->request()->get('locationId');
    
    $db = new DbHandler();
//echo "testing";
    // fetching all user tasks
    $result = $db->getLocationFollowers($locationId);

    // looping through result and preparing updates array
    if($result) {
        $response["error"] = false;
        $response["followers"] = array();
        $follower = $result->fetch_assoc();

        if($follower == NULL) {
            $response["message"] = "No Followers Found";
            echoResponse(200, $response);
            return;
        }
        // looping through result and preparing updates array
        while ($follower) {
            $tmp = array();
            $response["error"] = false;
            $tmp["follower"] = $follower;
            array_push($response["followers"], $tmp);
            
            $follower = $result->fetch_assoc();
        }
        $response["message"] = "Query Successful";
    } 
    else {
        $response["error"] = true;
        $response["message"] = "One or more field(s) missing or invalid.";
    }

    echoResponse(200, $response);
});

$app->get('/userlocations', function() use ($app){
    $response = array();
    $userId = $app->request()->get('userId');
    
    $db = new DbHandler();
//echo "testing";
    // fetching all user tasks
    $result = $db->getUserLocations($userId);

    // looping through result and preparing updates array
    if($result) {
        $response["error"] = false;
        $response["locations"] = array();
        $location = $result->fetch_assoc();

        if($location == NULL) {
            $response["message"] = "No Locations Found";
            echoResponse(200, $response);
            return;
        }
        // looping through result and preparing updates array
        while ($location) {
            $tmp = array();
            $response["error"] = false;
//            $tmp["locationId"] = $location;
            array_push($response["locations"], $location);
            
            $location = $result->fetch_assoc();
        }
        $response["message"] = "Query Successful";
    } 
    else {
        $response["error"] = true;
        $response["message"] = "One or more field(s) missing or invalid.";
    }

    echoResponse(200, $response);
});

    /*
    *  NOTIFICATION RELATED METHODS
    * 
    */

$app->get('/allnotifications', function() use ($app){
    $response = array();
    $userId = $app->request()->get('userId');
    
    $db = new DbHandler();
//echo "testing";
    // fetching all user tasks
    $result = $db->getAllNotifications($userId);

    // looping through result and preparing updates array
    if($result) {
        $response["error"] = false;
        $response["notifications"] = array();
        $notification = $result->fetch_assoc();

        if($notification == NULL) {
            $response["message"] = "No Notifications Found";
            echoResponse(200, $response);
            return;
        }
        // looping through result and preparing updates array
        while ($notification) {
            $tmp = array();
            $response["error"] = false;
            $tmp["notifId"] = $notification["id"];
            $tmp["notifFromId"] = $notification["notifFrom"];
            $tmp["notifToId"] = $notification["notifTo"];
            $tmp["notifFromUsername"] = $notification["username"];
            $tmp["notifType"] = $notification["notifType"];
            $tmp["notifAbout"] = $notification["notifAbout"];
            $tmp["notifAboutId"] = $notification["notifAboutId"];
            $tmp["timeOfNotification"] = $notification["timeOfNotification"];
            array_push($response["notifications"], $tmp);
            
            $notification = $result->fetch_assoc();
        }
        $response["message"] = "Query Successful";
    } 
    else {
        $response["error"] = true;
        $response["message"] = "Something is wrong here.";
    }

    echoResponse(200, $response);
});


//$app->post('/test', function() use ($app) {
//   $userId = $app->request()->post('userId');   
//   echo $userId;
//});

//$app->get('/test', function() use ($app){
//    
//    $response = array();
//    
//    $db = new DbHandler();
////echo "testing";
//    // fetching all user tasks
//    $locationFollowers = $db->test();
//
//        $id = $locationFollowers->fetch_assoc();
//        
//        if($id == NULL) {
//            $response["message"] = "No location follower Found";
//            echoResponse(200, $response);
//            return;
//        }
//        // looping through result and preparing updates array
//        while ($id) {
//            $response["error"] = false;
//            $response["id"] = $id["userId"];
//            
//            $id = $locationFollowers->fetch_assoc();
//        }
//        $response["message"] = "Query Successful";
//     
//
//    echoResponse(200, $response);
//        
//});



$app->run();

?>
