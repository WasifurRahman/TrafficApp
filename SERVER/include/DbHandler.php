<?php

/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

class DbHandler {
 
    private $conn;
 
    function __construct() {
        require_once dirname(__FILE__) . './DbConnect.php';
        // opening db connection
        $db = new DbConnect();
        $this->conn = $db->connect();
    }
 
    public function createUser($username, $gender, $email, $password) {
        require_once 'PassHash.php';
        $response = array();
 
        // First check if user already existed in db
        if (!$this->isUserExists($email)) {
            // Generating password hash
            $password_hash = PassHash::hash($password);
 
            // Generating API key
            $api_key = $this->generateApiKey();
 
            // insert query
            $stmt = $this->conn->prepare("INSERT INTO user(username, gender, email, password_hash, api_key, status) values(?, ?, ?, ?, ?, 1)");
            $stmt->bind_param("sssss", $username, $gender, $email, $password_hash, $api_key);
 
            $result = $stmt->execute();
 
            $stmt->close();
 
            // Check for successful insertion
            if ($result) {
                // User successfully inserted
                return USER_CREATED_SUCCESSFULLY;
            } else {
                // Failed to create user
                return USER_CREATE_FAILED;
            }
        } else {
            // User with same email already existed in the db
            return USER_ALREADY_EXISTED;
        }
 
        return $response;
    }
 
    
    public function checkLogin($email, $password) {
        // fetching user by email
        $stmt = $this->conn->prepare("SELECT password_hash FROM user WHERE email = ?");
 
        $stmt->bind_param("s", $email);
 
        $stmt->execute();
 
        $stmt->bind_result($password_hash);
 
        $stmt->store_result();
 
        if ($stmt->num_rows > 0) {
            // Found user with the email
            // Now verify the password
 
            $stmt->fetch();
 
            $stmt->close();
 
            if (PassHash::check_password($password_hash, $password)) {
                // User password is correct
                return TRUE;
            } else {
                // user password is incorrect
                return FALSE;
            }
        } else {
            $stmt->close();
 
            // user not existed with the email
            return FALSE;
        }
    }
 
    private function isUserExists($email) {
        $stmt = $this->conn->prepare("SELECT id from user WHERE email = ?");
        $stmt->bind_param("s", $email);
        $stmt->execute();
        $stmt->store_result();
        $num_rows = $stmt->num_rows;
        $stmt->close();
        return $num_rows > 0;
    }
 
    
    public function getUserByEmail($email) {
        $stmt = $this->conn->prepare("SELECT username, id, api_key FROM user WHERE email = ?");
        $stmt->bind_param("s", $email);
        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $user;
        } else {
            return NULL;
        }
    }
 
    public function getUserById($id) {
        $stmt = $this->conn->prepare("SELECT username, id, api_key FROM user WHERE id = ?");
        $stmt->bind_param("i", $id);
        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $user;
        } else {
            return NULL;
        }
    }
    
    public function getApiKeyById($user_id) {
        $stmt = $this->conn->prepare("SELECT api_key FROM user WHERE id = ?");
        $stmt->bind_param("i", $user_id);
        if ($stmt->execute()) {
            $api_key = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $api_key;
        } else {
            return NULL;
        }
    }
 
  
    public function getUserId($api_key) {
        $stmt = $this->conn->prepare("SELECT id FROM user WHERE api_key = ?");
        $stmt->bind_param("s", $api_key);
        if ($stmt->execute()) {
            $user_id = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $user_id;
        } else {
            return NULL;
        }
    }
 
     // If the api key is there in db, it is a valid key
     
    public function isValidApiKey($api_key) {
        $stmt = $this->conn->prepare("SELECT id from user WHERE api_key = ?");
        $stmt->bind_param("s", $api_key);
        $stmt->execute();
        $stmt->store_result();
        $num_rows = $stmt->num_rows;
        $stmt->close();
        return $num_rows > 0;
    }
 
    
     // Generating random Unique MD5 String for user Api key
     
    private function generateApiKey() {
        return md5(uniqid(rand(), true));
    }
    
    
    
    public function getLocation($id) {
        $stmt = $this->conn->prepare("SELECT name from location WHERE id = ?");
        $stmt->bind_param("s", $id);
        if ($stmt->execute()) {
            $location_name = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $location_name;
        } else {
            return NULL;
        }
    }
    
    /*
     *  UPDATE RELATED METHODS
     * 
     */
    
    public function addUpdate($fromLocationId, $toLocationId, $estTimeToCross, $situation, $description, $timeOfSituation, $updaterId, $requestId) {
 
        if($requestId == 0) {         // NORMAL UPDATE
            // insert query
            $stmt = $this->conn->prepare("INSERT INTO `update`(locationIdFrom, locationIdTo, estTimeToCross, situation, description, timeOfSituation, updaterId) values (?, ?, ?, ?, ?, ?, ?)");
            $stmt->bind_param("iiisssi", $fromLocationId, $toLocationId, $estTimeToCross, $situation, $description, $timeOfSituation, $updaterId);
            // $stmt->bind_param("sssssss", $fromLocationId, $toLocationId, $estTimeToCross, $situation, $description, $timeOfSituation, $updaterId);

            $result = $stmt->execute();

            $stmt->close();

            // Check for successful insertion
            if ($result) {
                // Update successfully added
                return 1;
            } else {
                // Failed to add Update
                return 0;
            }
        }
        else {                      // RESPONSE TO REQUEST
            // insert query
            $stmt = $this->conn->prepare("INSERT INTO `update`(locationIdFrom, locationIdTo, estTimeToCross, situation, description, timeOfSituation, updaterId, requestId) values (?, ?, ?, ?, ?, ?, ?, ?)");
            $stmt->bind_param("iiisssii", $fromLocationId, $toLocationId, $estTimeToCross, $situation, $description, $timeOfSituation, $updaterId, $requestId);
            
            $result = $stmt->execute();

            $stmt->close();
            
            // get the update id
//            $stmt = $this->conn->prepare("SELECT id FROM `update`(locationIdFrom, locationIdTo, estTimeToCross, situation, description, timeOfSituation, updaterId, requesterId) values (?, ?, ?, ?, ?, ?, ?, ?)");
//            $stmt->bind_param("iiisssii", $fromLocationId, $toLocationId, $estTimeToCross, $situation, $description, $timeOfSituation, $updaterId, $requesterId);
//            $result = $stmt->execute();
//
//            $stmt->close();
            
            // Get requester id
            $stmt = $this->conn->prepare("SELECT requesterId FROM request WHERE id = ? ");
//            echo $this->conn->error;
            $stmt->bind_param("i", $requestId);
            $stmt->execute();
            $stmt->bind_result($requesterId);
            $stmt->store_result();
            
            $stmt->fetch();
            $stmt->close();
            
            // Notify the requester about the new response
            
            if($requesterId != $updaterId) {
                $stmt = $this->conn->prepare("INSERT INTO `notification`(notifTo, notifFrom, notifType, notifAbout, notifAboutId) values (?, ?, ?, ?, ? );");
//            echo $this->conn->error;
                $notifType='requestResponse';
                $notifAbout = 'request';
                $stmt->bind_param("iissi", $requesterId, $updaterId, $notifType, $notifAbout, $requestId);

                $stmt->execute();

                $stmt->close();
            }
            
            // Get request followers
            $stmt = $this->conn->prepare("SELECT userId AS followerId FROM request_followeruser WHERE requestId = ? ");
//            echo $this->conn->error;
            $stmt->bind_param("i", $requestId);
            $stmt->execute();
            $followers = $stmt->get_result();
            $stmt->close();
            
            // Notify request followers
            while ($follower = $followers->fetch_assoc()) {
                $notifTo = $follower["followerId"];
                $stmt = $this->conn->prepare("INSERT INTO `notification`(notifTo, notifFrom, notifType, notifAbout, notifAboutId) values (?, ?, ?, ?, ? );");
//              echo $this->conn->error;
                $notifType='followResponse';
                $notifAbout = 'request';
                $stmt->bind_param("iissi", $notifTo, $updaterId, $notifType, $notifAbout, $requestId);

                $stmt->execute();

                $stmt->close();
            }
            

            // Check for successful insertion
            if ($result) {
                // Update successfully added
                return 1;
            } else {
                // Failed to add Update
                return 0;
            }
        }
        
    
    }
   /* 
    public function getAllUpdates($sortType) {
        
        if($sortType == "mostRecent") {
//            echo "1";
            $stmt = $this->conn->prepare("SELECT DISTINCT up.*, u.username, liker.username AS likers, disliker.username AS dislikers, uLike.userId AS likersId, uDislike.userId AS dislikersId "
                    . "FROM `update` up, `user` u, `user` liker, `user` disliker, update_voteruser uLike, update_voteruser uDislike "
                    . "WHERE up.updaterId = u.id AND uLike.voteType = 'like' AND uDislike.voteType = 'dislike' "
                    . "ORDER BY up.timeOfUpdate DESC;");
//            echo "1";
        } else if ($sortType == "mostPopular") {
            $stmt = $this->conn->prepare("SELECT DISTINCT up.*, u.username, liker.username AS likers, disliker.username AS dislikers, uLike.userId AS likersId, uDislike.userId AS dislikersId "
                    . "FROM `update` up, `user` u, `user` liker, `user` disliker, update_voteruser uLike, update_voteruser uDislike "
                    . "WHERE up.updaterId = u.id AND uLike.voteType = 'like' AND uDislike.voteType = 'dislike' AND up.updaterId = 10 "
                    . "ORDER BY up.likeCount DESC;");
        }
         
        $stmt->execute();
//        $meta = $stmt->result_metadata();
        
        $update = array();
        
        $stmt->bind_result($update, $updaterNames, $likers, $dislikers, $likersId, $dislikersId);
//        while ($field) {
//          $updates[] = $meta->fetch_field();
//        }
        
//        $updates = $stmt->get_result();
        $updates = array($update, $updaterNames, $likers, $dislikers, $likersId, $dislikersId);
        $stmt->close();
        return $updates;
    }
    */
    
    public function getAllUpdates($sortType) {
        if($sortType == "mostRecent") {
            $stmt = $this->conn->prepare("SELECT up.*, u.username "
                    . "FROM `update` up, `user` u "
                    . "WHERE up.updaterId = u.id "
                    . "ORDER BY up.timeOfUpdate DESC;");
        } else if ($sortType == "mostPopular") {
            $stmt = $this->conn->prepare("SELECT up.*, u.username "
                    . "FROM `update` up, `user` u "
                    . "WHERE up.updaterId = u.id "
                    . "ORDER BY up.likeCount DESC;");
        }
        
//        echo $this->conn->error;
        $stmt->execute();
        $updates = $stmt->get_result();
        $stmt->close();
        
        return $updates;
    }
    
    public function getUpdateLikers($update) {
        
            $uid = $update["id"];
//            echo "updateid: " . $uid . "\n";
            
            $stmt = $this->conn->prepare("SELECT uv.userId AS likerId, u.username AS likerName FROM update_voteruser uv, `user` u "
                    . "WHERE uv.userId = u.id AND uv.voteType = 'like' AND updateId = ? ;");
            $stmt->bind_param("i", $uid);
            $stmt->execute();
            $likers = $stmt->get_result();
            $stmt->close();
        
            return $likers;
    }
    
    public function getUpdateComments($updateId) {
        
            $stmt = $this->conn->prepare("SELECT uc.*, u.username AS commenterName "
                    . "FROM update_commenteruser uc, `user` u "
                    . "WHERE updateId = ? AND uc.commenterId = u.id ORDER BY timeOfComment ASC");
            
            $stmt->bind_param("i", $updateId);
            $stmt->execute();
            $comments = $stmt->get_result();
            $stmt->close();
        
            return $comments;
    }
    
    public function getUpdateDislikers($update) {
        
            $uid = $update["id"];
//            echo "updateid: " . $uid . "\n";
            
            $stmt = $this->conn->prepare("SELECT uv.userId AS dislikerId, u.username AS dislikerName FROM update_voteruser uv, `user` u "
                    . "WHERE uv.userId = u.id AND uv.voteType = 'dislike' AND updateId = ? ;");
            $stmt->bind_param("i", $uid);
            $stmt->execute();
            $dislikers = $stmt->get_result();
            $stmt->close();
        
            return $dislikers;
    }
    
    
    
    public function addUpdateLike($updateId, $likerId) {
         $stmt = $this->conn->prepare("INSERT INTO update_voteruser (userId, updateId, voteType) values (?, ?, 'like');");
         $stmt->bind_param("ii", $likerId, $updateId);
         $stmt->execute();
         $stmt->close();
         
         $stmt = $this->conn->prepare("SELECT updaterId FROM `update` WHERE id = ? ;");
         $stmt->bind_param("i", $updateId);
         $stmt->execute();
         $stmt->bind_result($updaterId);
         $stmt->store_result();
         $stmt->fetch();
         $stmt->close();
         
         if($updaterId != $likerId) {
            $stmt = $this->conn->prepare("INSERT INTO notification (notifTo, notifFrom, notifType, notifAbout, notifAboutId) values ( ?, ?, 'like', 'update', ? );");
            $stmt->bind_param("iii", $updaterId, $likerId, $updateId);
            $stmt->execute();
            $stmt->close();
         }
         
         $stmt = $this->conn->prepare("UPDATE `update` SET likeCount = likeCount + 1 WHERE id = ?;");
         $stmt->bind_param("i", $updateId);
         $result = $stmt->execute();
         $stmt->close();
         return $result;
    }
    
    public function addUpdateComment($updateId, $commenterId, $commentText) {
         $stmt = $this->conn->prepare("INSERT INTO update_commenteruser (commenterId, updateId, commentText) values (?, ?, ? );");
         $stmt->bind_param("iis", $commenterId, $updateId, $commentText);
         $result = $stmt->execute();
         $stmt->close();
         
         $stmt = $this->conn->prepare("SELECT updaterId FROM `update` WHERE id = ? ;");
         $stmt->bind_param("i", $updateId);
         $stmt->execute();
         $stmt->bind_result($updaterId);
         $stmt->store_result();
         $stmt->fetch();
         $stmt->close();
         
         if($updaterId != $commenterId) {
            $stmt = $this->conn->prepare("INSERT INTO notification (notifTo, notifFrom, notifType, notifAbout, notifAboutId) values ( ?, ?, 'comment', 'update', ? );");
            $stmt->bind_param("iii", $updaterId, $commenterId, $updateId);
            $stmt->execute();
            $stmt->close();
         }
         
         //get other commenters for this update
         
         $stmt = $this->conn->prepare("SELECT commenterId FROM update_commenteruser WHERE updateId = ? ");
//            echo $this->conn->error;
            $stmt->bind_param("i", $updateId);
            $stmt->execute();
            $commenters = $stmt->get_result();
            $stmt->close();
            
            // Notify request followers
            while ($oldCommenter = $commenters->fetch_assoc()) {
                $notifTo = $oldCommenter["commenterId"];
                if($notifTo != $commenterId && $notifTo != $updaterId) {
                    $stmt = $this->conn->prepare("INSERT INTO `notification`(notifTo, notifFrom, notifType, notifAbout, updateId) values (?, ?, ?, ?, ? );");
    //              echo $this->conn->error;
                    $notifType='commentResponse';
                    $notifAbout = 'update';
                    $stmt->bind_param("iissi", $notifTo, $commenterId, $notifType, $notifAbout, $updateId);

                    $stmt->execute();

                    $stmt->close();
                }
            }
         
         return $result;
    }
    
    public function removeUpdateLike($updateId, $likerId) {
         $stmt = $this->conn->prepare("DELETE FROM update_voteruser WHERE userId = ? AND updateId = ? ;");
//         echo $this->conn->error;
         $stmt->bind_param("ii", $likerId, $updateId);
         $stmt->execute();
         $stmt->close();
         
//         $stmt = $this->conn->prepare("SELECT updaterId FROM `update` WHERE id = ? ;");
//         $stmt->bind_param("i", $updateId);
//         $stmt->execute();
//         $stmt->bind_result($updaterId);
//         $stmt->store_result();
//         $stmt->fetch();
//         $stmt->close();
//         
//         $stmt = $this->conn->prepare("INSERT INTO notification (notifTo, notifFrom, notifType, notifAbout) values ( ?, ?, 'like', 'update');");
//         $stmt->bind_param("ii", $updaterId, $likerId);
//         $stmt->execute();
//         $stmt->close();
         
         $stmt = $this->conn->prepare("UPDATE `update` SET likeCount = likeCount - 1 WHERE id = ?;");
         $stmt->bind_param("i", $updateId);
         $result = $stmt->execute();
         $stmt->close();
         return $result;
    }
    
    public function addUpdateDislike($updateId, $dislikerId) {
         $stmt = $this->conn->prepare("INSERT INTO update_voteruser (userId, updateId, voteType) values (?, ?, 'dislike');");
//         echo $this->conn->error;
         $stmt->bind_param("ii", $dislikerId, $updateId);
         $stmt->execute();
         $stmt->close();
         
//         $stmt = $this->conn->prepare("SELECT updaterId FROM `update` WHERE id = ? ;");
////         echo $this->conn->error;
//         $stmt->bind_param("i", $updateId);
//         $stmt->execute();
//         $stmt->bind_result($updaterId);
//         $stmt->store_result();
//         $stmt->fetch();
//         $stmt->close();
//         
//         if($updaterId != $dislikerId) {
//             $stmt = $this->conn->prepare("INSERT INTO notification (notifTo, notifFrom, notifType, notifAbout) values ( ?, ?, 'dislike', 'update');");
////         echo $this->conn->error;
//         $stmt->bind_param("ii", $updaterId, $dislikerId);
//         $stmt->execute();
//         $stmt->close();
//         }
         
         $stmt = $this->conn->prepare("UPDATE `update` SET dislikeCount = dislikeCount + 1 WHERE id = ?;");
//         echo $this->conn->error;
         $stmt->bind_param("i", $updateId);
         $result = $stmt->execute();
         $stmt->close();
         return $result;
    }
    
    public function removeUpdateDislike($updateId, $dislikerId) {
         $stmt = $this->conn->prepare("DELETE FROM update_voteruser WHERE userId = ? AND updateId = ? ;");
         $stmt->bind_param("ii", $dislikerId, $updateId);
         $stmt->execute();
         $stmt->close();
         
//         $stmt = $this->conn->prepare("SELECT updaterId FROM `update` WHERE id = ? ;");
//         $stmt->bind_param("i", $updateId);
//         $stmt->execute();
//         $stmt->bind_result($updaterId);
//         $stmt->store_result();
//         $stmt->fetch();
//         $stmt->close();
//         
//         $stmt = $this->conn->prepare("INSERT INTO notification (notifTo, notifFrom, notifType, notifAbout) values ( ?, ?, 'like', 'update');");
//         $stmt->bind_param("ii", $updaterId, $likerId);
//         $stmt->execute();
//         $stmt->close();
         
         $stmt = $this->conn->prepare("UPDATE `update` SET dislikeCount = dislikeCount - 1 WHERE id = ?;");
         $stmt->bind_param("i", $updateId);
         $result = $stmt->execute();
         $stmt->close();
         return $result;
    }
    
    public function getUpdatesFromLocation($sortType, $locationId) {
        if($sortType == "mostRecent") {
            $stmt = $this->conn->prepare("SELECT up.*, u.username "
                    . "FROM `update` up, `user` u "
                    . "WHERE up.updaterId = u.id AND (locationIdFrom = ? OR locationIdTo = ? ) "
                    . "ORDER BY up.timeOfUpdate DESC;");
        } else if ($sortType == "mostPopular") {
            $stmt = $this->conn->prepare("SELECT up.*, u.username "
                    . "FROM `update` up, `user` u "
                    . "WHERE up.updaterId = u.id AND (locationIdFrom = ? OR locationIdTo = ? ) "
                    . "ORDER BY up.likeCount DESC;");
        }
//        $this->conn->error;
        $stmt->bind_param("ii", $locationId, $locationId);
        $stmt->execute();
        $updates = $stmt->get_result();
        $stmt->close();
        return $updates;
    }
    
    public function getUpdateById($updateId) {
        
        $stmt = $this->conn->prepare("SELECT up.*, u.username "
                . "FROM `update` up, `user` u "
                . "WHERE up.id =  ? ");
        
//        $this->conn->error;
        $stmt->bind_param("i", $updateId);
        $stmt->execute();
        $update = $stmt->get_result();
        $stmt->close();
        return $update;
    }
    
    
    public function getUserUpdates($sortType, $userId) {
        if($sortType == "mostRecent") {
            $stmt = $this->conn->prepare("SELECT * from `update` WHERE updaterId = ? ORDER BY timeOfUpdate DESC;");
        } else if ($sortType == "mostPopular") {
            $stmt = $this->conn->prepare("SELECT * from `update` WHERE updaterId = ? ORDER BY likeCount DESC;");
        }
        
        $stmt->bind_param("s", $userId);
        $stmt->execute();
//        $stmt->bind_result($password_hash);
//        $stmt->store_result();
//        $updates = $stmt->fetch_assoc();
//        $stmt->free_result();
        
        $updates = $stmt->get_result();
        
        $stmt->close();
        return $updates;
    }
    
/*
 *  POST RELATED METHODS
 *
 */

    
    public function addPost($postType, $locationId, $description, $posterId, $title, $source) {
        // insert query
        if($postType == "discussion") {
            $stmt = $this->conn->prepare("INSERT INTO `post`(type, locationId, description, posterId) values (?, ?, ?, ?)");
            $stmt->bind_param("sisi", $postType, $locationId, $description, $posterId);
        } else if($postType == "announcement") {
            $stmt = $this->conn->prepare("INSERT INTO `post`(type, locationId, description, posterId, title, source) values (?, ?, ?, ?, ?, ?)");
            $stmt->bind_param("sisiss", $postType, $locationId, $description, $posterId, $title, $source);
        }

        $result = $stmt->execute();

        $stmt->close();

        // Check for successful insertion
        if ($result) {
            // Post successfully added
            return 1;
        } else {
            // Failed to add Post
            return 0;
        }
    }
    
    public function addPostComment($postId, $commenterId, $commentText) {
         $stmt = $this->conn->prepare("INSERT INTO post_commenteruser (commenterId, postId, commentText) values (?, ?, ? );");
         $stmt->bind_param("iis", $commenterId, $postId, $commentText);
         $result = $stmt->execute();
         $stmt->close();
         
         $stmt = $this->conn->prepare("SELECT posterId FROM `post` WHERE id = ? ;");
         $stmt->bind_param("i", $postId);
         $stmt->execute();
         $stmt->bind_result($posterId);
         $stmt->store_result();
         $stmt->fetch();
         $stmt->close();
         
         if($posterId != $commenterId) {
            $stmt = $this->conn->prepare("INSERT INTO notification (notifTo, notifFrom, notifType, notifAbout, notifAboutId) values ( ?, ?, 'comment', 'post', ? );");
            $stmt->bind_param("iii", $posterId, $commenterId, $postId);
            $stmt->execute();
            $stmt->close();
         }
         
         //get other commenters for this update
         
         $stmt = $this->conn->prepare("SELECT commenterId FROM post_commenteruser WHERE postId = ? ");
//            echo $this->conn->error;
            $stmt->bind_param("i", $postId);
            $stmt->execute();
            $commenters = $stmt->get_result();
            $stmt->close();
            
            // Notify request followers
            while ($oldCommenter = $commenters->fetch_assoc()) {
                $notifTo = $oldCommenter["commenterId"];
                if($notifTo != $commenterId && $notifTo != $posterId) {
                    $stmt = $this->conn->prepare("INSERT INTO `notification`(notifTo, notifFrom, notifType, notifAbout, notifAboutId) values (?, ?, ?, ?, ? );");
    //              echo $this->conn->error;
                    $notifType='commentResponse';
                    $notifAbout = 'post';
                    $stmt->bind_param("iissi", $notifTo, $commenterId, $notifType, $notifAbout, $postId);

                    $stmt->execute();

                    $stmt->close();
                }
            }
         
         return $result;
    }
    
    public function getAllPosts($sortType, $postType) {
        
        if($postType) {
            if($sortType == "mostRecent") {
                $stmt = $this->conn->prepare("SELECT p.*, u.username "
                    . "FROM `post` p, `user` u "
                    . "WHERE p.posterId = u.id AND p.type = ? "
                    . "ORDER BY p.timeOfPost DESC;");
            } else if ($sortType == "mostPopular") {
                $stmt = $this->conn->prepare("SELECT p.*, u.username "
                    . "FROM `post` p, `user` u "
                    . "WHERE p.posterId = u.id AND p.type = ? "
                    . "ORDER BY p.likeCount DESC;");
            }
            $stmt->bind_param("s", $postType);
        }
        else {
            if($sortType == "mostRecent") {
                $stmt = $this->conn->prepare("SELECT p.*, u.username "
                    . "FROM `post` p, `user` u "
                    . "WHERE p.posterId = u.id "
                    . "ORDER BY p.timeOfPost DESC;");
            } else if ($sortType == "mostPopular") {
                $stmt = $this->conn->prepare("SELECT p.*, u.username "
                    . "FROM `post` p, `user` u "
                    . "WHERE p.posterId = u.id "
                    . "ORDER BY p.likeCount DESC;");
            }
        }
//        echo $this->conn->error;

        $stmt->execute();
        $posts = $stmt->get_result();
        $stmt->close();
        return $posts;
    }
    
    public function getPostById($postId) {
        
        $stmt = $this->conn->prepare("SELECT up.*, u.username "
                . "FROM `post` up, `user` u "
                . "WHERE up.id =  ? ");
        
//        $this->conn->error;
        $stmt->bind_param("i", $postId);
        $stmt->execute();
        $post = $stmt->get_result();
        $stmt->close();
        return $post;
    }
    
    public function getPostComments($postId) {
        
            $stmt = $this->conn->prepare("SELECT uc.*, u.username AS commenterName "
                    . "FROM post_commenteruser uc, `user` u "
                    . "WHERE postId = ? AND uc.commenterId = u.id ORDER BY timeOfComment ASC");
            
            $stmt->bind_param("i", $postId);
            $stmt->execute();
            $comments = $stmt->get_result();
            $stmt->close();
        
            return $comments;
    }
    
    public function getPostLikers($post) {
        
            $uid = $post["id"];
//            echo "updateid: " . $uid . "\n";
            
            $stmt = $this->conn->prepare("SELECT pv.userId AS likerId, u.username AS likerName FROM post_voteruser pv, `user` u "
                    . "WHERE pv.userId = u.id AND pv.voteType = 'like' AND postId = ? ;");
            $stmt->bind_param("i", $uid);
            $stmt->execute();
            $likers = $stmt->get_result();
            $stmt->close();
        
            return $likers;
    }
    
    public function getPostDislikers($post) {
        
            $uid = $post["id"];
//            echo "updateid: " . $uid . "\n";
            
            $stmt = $this->conn->prepare("SELECT pv.userId AS dislikerId, u.username AS dislikerName FROM post_voteruser pv, `user` u "
                    . "WHERE pv.userId = u.id AND pv.voteType = 'dislike' AND postId = ? ;");
            $stmt->bind_param("i", $uid);
            $stmt->execute();
            $dislikers = $stmt->get_result();
            $stmt->close();
        
            return $dislikers;
    }
    
    public function addPostLike($postId, $likerId) {
         $stmt = $this->conn->prepare("INSERT INTO post_voteruser (userId, postId, voteType) values (?, ?, 'like');");
         $stmt->bind_param("ii", $likerId, $postId);
         $result = $stmt->execute();
         $stmt->close();
         
         $stmt = $this->conn->prepare("SELECT posterId FROM post WHERE id = ? ;");
         $stmt->bind_param("i", $postId);
         $stmt->execute();
         $stmt->bind_result($posterId);
         $stmt->store_result();
         $stmt->fetch();
         $stmt->close();
         
         if ($posterId != $likerId) {
            $stmt = $this->conn->prepare("INSERT INTO notification (notifTo, notifFrom, notifType, notifAbout, notifAboutId) values ( ?, ?, 'like', 'post', ? );");
            $stmt->bind_param("iii", $posterId, $likerId, $postId);
            $stmt->execute();
            $stmt->close();
         }
         
         $stmt = $this->conn->prepare("UPDATE `post` SET likeCount = likeCount + 1 WHERE id = ?;");
         $stmt->bind_param("i", $postId);
         $stmt->execute();
         $stmt->close();
         
         if($result)
             return 1;
         else
             return 0;
    }
    
    public function removePostLike($postId, $likerId) {
         $stmt = $this->conn->prepare("DELETE FROM post_voteruser WHERE userId = ? AND postId = ? ;");
         $stmt->bind_param("ii", $likerId, $postId);
         $result = $stmt->execute();
         $stmt->close();
         
         $stmt = $this->conn->prepare("UPDATE `post` SET likeCount = likeCount - 1 WHERE id = ? ;");
         $stmt->bind_param("i", $postId);
         $stmt->execute();
         $stmt->close();
         
         if($result)
             return 1;
         else
             return 0;
    }
    
    public function addPostDislike($postId, $dislikerId) {
         $stmt = $this->conn->prepare("INSERT INTO post_voteruser (userId, postId, voteType) values (?, ?, 'dislike');");
         $stmt->bind_param("ii", $dislikerId, $postId);
         $result = $stmt->execute();
         $stmt->close();
         
//         $stmt = $this->conn->prepare("SELECT posterId FROM post WHERE id = ? ;");
//         $stmt->bind_param("i", $postId);
//         $stmt->execute();
//         $stmt->bind_result($posterId);
//         $stmt->store_result();
//         $stmt->fetch();
//         $stmt->close();
//         
//         $stmt = $this->conn->prepare("INSERT INTO notification (notifTo, notifFrom, notifType, notifAbout) values ( ?, ?, 'dislike', 'post');");
//         $stmt->bind_param("ii", $posterId, $dislikerId);
//         $stmt->execute();
//         $stmt->close();
         
         $stmt = $this->conn->prepare("UPDATE `post` SET dislikeCount = dislikeCount + 1 WHERE id = ?;");
         $stmt->bind_param("i", $postId);
         $stmt->execute();
         $stmt->close();
         
         if($result)
             return 1;
         else
             return 0;
    }
    
    public function removePostDislike($postId, $dislikerId) {
         $stmt = $this->conn->prepare("DELETE FROM post_voteruser WHERE userId = ? AND postId = ? ;");
         $stmt->bind_param("ii", $dislikerId, $postId);
         $result = $stmt->execute();
         $stmt->close();
         
         $stmt = $this->conn->prepare("UPDATE `post` SET dislikeCount = dislikeCount - 1 WHERE id = ? ;");
         $stmt->bind_param("i", $postId);
         $stmt->execute();
         $stmt->close();
         
         if($result)
             return 1;
         else
             return 0;
    }
    
    public function getPostsFromLocation($sortType, $postType, $locationId) {
        if($postType) {
            if($sortType == "mostRecent") {
                $stmt = $this->conn->prepare("SELECT p.*, u.username "
                    . "FROM `post` p, `user` u "
                    . "WHERE p.posterId = u.id AND p.type = ? AND p.locationId = ? "
                    . "ORDER BY p.timeOfPost DESC;");
            } else if ($sortType == "mostPopular") {
                $stmt = $this->conn->prepare("SELECT p.*, u.username "
                    . "FROM `post` p, `user` u "
                    . "WHERE p.posterId = u.id AND p.type = ? AND p.locationId = ? "
                    . "ORDER BY p.likeCount DESC;");
            }
            $stmt->bind_param("ss", $postType, $locationId);
        }
        else {
            if($sortType == "mostRecent") {
                $stmt = $this->conn->prepare("SELECT p.*, u.username "
                    . "FROM `post` p, `user` u "
                    . "WHERE p.posterId = u.id AND p.locationId = ? "
                    . "ORDER BY p.timeOfPost DESC;");
            } else if ($sortType == "mostPopular") {
                $stmt = $this->conn->prepare("SELECT p.*, u.username "
                    . "FROM `post` p, `user` u "
                    . "WHERE p.posterId = u.id AND p.locationId = ? "
                    . "ORDER BY p.timeOfPost DESC;");
            }
            $stmt->bind_param("s", $locationId);
        }
        

        $stmt->execute();
        $posts = $stmt->get_result();
        $stmt->close();
        return $posts;
    }
    
    public function getUserPosts($sortType, $postType, $userId) {
        if($postType) {
            if($sortType == "mostRecent") {
                $stmt = $this->conn->prepare("SELECT * from `post` WHERE type = ? AND posterId = ? ORDER BY timeOfPost DESC;");
            } else if ($sortType == "mostPopular") {
                $stmt = $this->conn->prepare("SELECT * from `post` WHERE type = ? AND posterId = ? ORDER BY likeCount DESC;");
            }
            $stmt->bind_param("ss", $postType, $userId);
        }
        else {
            if($sortType == "mostRecent") {
                $stmt = $this->conn->prepare("SELECT * from `post` WHERE posterId = ? ORDER BY timeOfPost DESC;");
            } else if ($sortType == "mostPopular") {
                $stmt = $this->conn->prepare("SELECT * from `post` WHERE perId = ? ORDER BY likeCount DESC;");
            }
            $stmt->bind_param("s", $userId);
        }
        

        $stmt->execute();
        $posts = $stmt->get_result();
        $stmt->close();
        return $posts;
    }
    
    
    /*
     *  REQUEST RELATED METHODS
     * 
     */
    
    public function addRequest($fromLocationId, $toLocationId, $description, $requesterId) {
        // insert query
        $stmt = $this->conn->prepare("INSERT INTO `request`(locationIdFrom, locationIdTo, description, requesterId) values (?, ?, ?, ?)");
        $stmt->bind_param("iisi", $fromLocationId, $toLocationId, $description, $requesterId);

        $result = $stmt->execute();
        $requestId = $stmt->insert_id;
        $stmt->close();
        
        $stmt = $this->conn->prepare("SELECT DISTINCT userId FROM `location_user` WHERE locationId = ? OR locationId = ? ");
        $stmt->bind_param("ii", $fromLocationId, $toLocationId);
        $stmt->execute();
        $locationFollowers = $stmt->get_result();

        $stmt->close();
        
        while($userId = $locationFollowers->fetch_assoc()) {
            $notifTo = $userId["userId"];
            if ($notifTo != $requesterId) {
                $stmt = $this->conn->prepare("INSERT INTO `notification`(notifTo, notifFrom, notifType, notifAbout, notifAboutId) values (?, ?, ?, ?, ? )");
//            echo $this->conn->error;
                $notifInfo = 'request';
                $stmt->bind_param("iissi", $notifTo, $requesterId, $notifInfo, $notifInfo, $requestId);

                $stmt->execute();

                $stmt->close();
            }
        }
        
      
        // Check for successful insertion
        if ($result) {
            // Request successfully added
            return 1;
        } else {
            // Failed to add Request
            return 0;
        }
    }
    
    
    public function getAllRequests($sortType) {
        
        if($sortType == "mostRecent") {
            $stmt = $this->conn->prepare("SELECT r.*, u.username "
                    . "FROM `request` r, `user` u "
                    . "WHERE r.requesterId = u.id "
                    . "ORDER BY r.timeOfRequest DESC;");
        } else if ($sortType == "mostPopular") {
            $stmt = $this->conn->prepare("SELECT r.*, u.username "
                    . "FROM `request` r, `user` u "
                    . "WHERE r.requesterId = u.id "
                    . "ORDER BY r.followerCount DESC;");
        }
        
         
        $stmt->execute();
        $requests = $stmt->get_result();
        $stmt->close();
        return $requests;
    }
    
    public function getRequestById($requestId) {
        
        $stmt = $this->conn->prepare("SELECT up.*, u.username "
                . "FROM `request` up, `user` u "
                . "WHERE up.id =  ? ");
        
//        $this->conn->error;
        $stmt->bind_param("i", $requestId);
        $stmt->execute();
        $request = $stmt->get_result();
        $stmt->close();
        return $request;
    }
    
    public function getFollowers($request) {
        
            $rid = $request["id"];
//            echo "updateid: " . $uid . "\n";
            
            $stmt = $this->conn->prepare("SELECT rf.userId AS followerId, u.username AS followerName FROM request_followeruser rf, `user` u "
                    . "WHERE rf.userId = u.id AND requestId = ? ;");
            $stmt->bind_param("i", $rid);
            $stmt->execute();
            $followers = $stmt->get_result();
            $stmt->close();
        
            return $followers;
    }
    
    public function addRequestFollower($requestId, $followerId) {
         $stmt = $this->conn->prepare("INSERT INTO request_followeruser (userId, requestId) values (?, ?);");
         $stmt->bind_param("ii", $followerId, $requestId);
         $result = $stmt->execute();
         $stmt->close();
         
         $stmt = $this->conn->prepare("UPDATE `request` SET followerCount = followerCount + 1 WHERE id = ?;");
         $stmt->bind_param("i", $requestId);
         $stmt->execute();
         $stmt->close();
         
         $stmt = $this->conn->prepare("SELECT requesterId FROM `request` WHERE id = ? ;");
         $stmt->bind_param("i", $requestId);
         $stmt->execute();
         $stmt->bind_result($requesterId);
         $stmt->store_result();
         $stmt->fetch();
         $stmt->close();
         
        $stmt = $this->conn->prepare("INSERT INTO notification (notifTo, notifFrom, notifType, notifAbout, notifAboutId) values ( ?, ?, 'follow', 'request', ? );");
        $stmt->bind_param("iii", $requesterId, $followerId, $requestId);
        $stmt->execute();
        $stmt->close();
         
         
         $stmt = $this->conn->prepare("UPDATE `update` SET likeCount = likeCount + 1 WHERE id = ?;");
         $stmt->bind_param("i", $updateId);
         $result = $stmt->execute();
         $stmt->close();
         
         if($result)
             return 1;
         else
             return 0;
    }
    
    public function removeRequestFollower($requestId, $followerId) {
         $stmt = $this->conn->prepare("DELETE FROM request_followeruser WHERE userId = ? AND requestId = ? ;");
         $stmt->bind_param("ii", $followerId, $requestId);
         $stmt->execute();
         $stmt->close();
         
         $stmt = $this->conn->prepare("UPDATE `request` SET followerCount = followerCount - 1 WHERE id = ? ;");
         $stmt->bind_param("i", $requestId);
         $stmt->execute();
         $stmt->close();
    }

    public function getRequestsFromLocation($sortType, $locationId) {
        if($sortType == "mostRecent") {
            $stmt = $this->conn->prepare("SELECT r.*, u.username "
                    . "FROM `request` r, `user` u "
                    . "WHERE r.requesterId = u.id AND (locationIdFrom = ? OR locationIdTo = ? ) "
                    . "ORDER BY r.timeOfRequest DESC;"); 
        } else if ($sortType == "mostPopular") {
            $stmt = $this->conn->prepare("SELECT r.*, u.username "
                    . "FROM `request` r, `user` u "
                    . "WHERE r.requesterId = u.id AND (locationIdFrom = ? OR locationIdTo = ? ) "
                    . "ORDER BY r.followerCount DESC;"); 
        }
        
        $stmt->bind_param("ss", $locationId, $locationId);
        $stmt->execute();
        $requests = $stmt->get_result();
        $stmt->close();
        return $requests;
    }
    
    public function getUserRequests($sortType, $userId) {
        if($sortType == "mostRecent") {
            $stmt = $this->conn->prepare("SELECT * from `request` WHERE requesterId = ? ORDER BY timeOfRequest DESC;");
        } else if ($sortType == "mostPopular") {
            $stmt = $this->conn->prepare("SELECT * from `request` WHERE requesterId = ? ORDER BY followerCount DESC;");
        }
        
        $stmt->bind_param("s", $userId);
        $stmt->execute();
//        $stmt->bind_result($password_hash);
//        $stmt->store_result();
//        $updates = $stmt->fetch_assoc();
//        $stmt->free_result();
        
        $requests = $stmt->get_result();
        
        $stmt->close();
        return $requests;
    }
    
    /*
     *  LOCATION RELATED METHODS
     * 
     */
    
    public function getLocations() {
        $stmt = $this->conn->prepare("SELECT * FROM `location` ORDER BY id ASC;");
        
        $stmt->execute();
        $locations = $stmt->get_result();
        $stmt->close();
        return $locations;
    }
    
    public function followLocation($locationId, $followerId) {
        // insert query
        $stmt = $this->conn->prepare("INSERT INTO `location_user`(locationId, userId) values (?, ?)");
        $stmt->bind_param("ii", $locationId, $followerId);

        $result = $stmt->execute();

        $stmt->close();

        // Check for successful insertion
        if ($result) {
            // Request successfully added
            return 1;
        } else {
            // Failed to add Request
            return 0;
        }
    }
    
    public function unfollowLocation($locationId, $followerId) {
        // insert query
        $stmt = $this->conn->prepare("DELETE FROM `location_user` WHERE userId = ? AND locationId = ? ");
        $stmt->bind_param("ii",$followerId, $locationId);

        $result = $stmt->execute();

        $stmt->close();

        // Check for successful insertion
        if ($result) {
            // Request successfully added
            return 1;
        } else {
            // Failed to add Request
            return 0;
        }
    }
    
    public function getLocationFollowers($locationId) {
        $stmt = $this->conn->prepare("SELECT lu.userId, u.username FROM `location_user` lu, `user` u WHERE lu.locationId = ? AND u.id = lu.userId ORDER BY userId ASC;");
        
        $stmt->bind_param("i", $locationId);
        $stmt->execute();
        $followers = $stmt->get_result();
        $stmt->close();
        return $followers;
    }
    
    public function getUserLocations($userId) {
        $stmt = $this->conn->prepare("SELECT locationId FROM `location_user` WHERE userId = ? ;");
        
        $stmt->bind_param("i", $userId);
        $stmt->execute();
        $locations = $stmt->get_result();
        $stmt->close();
        return $locations;
    }
    
    /*
     *  NOTIFICATION RELATED METHODS
     * 
     */
    
    public function getAllNotifications($userId) {
        $stmt = $this->conn->prepare("SELECT n.*, notifGenerator.username FROM notification n, `user` notifGenerator "
                                    . "WHERE n.notifTo = ? AND n.notifFrom = notifGenerator.id "
                                    . "ORDER BY n.timeOfNotification DESC;");
        
        $stmt->bind_param("i", $userId);
        $stmt->execute();
        $notifications = $stmt->get_result();
        $stmt->close();
        return $notifications;
    }
    
    
//    public function test() {
//        $stmt = $this->conn->prepare("SELECT DISTINCT userId FROM `location_user` WHERE locationId = 1 OR locationId = 3 ");
////        $stmt->bind_param("ii", $fromLocationId, $toLocationId);
//        $stmt->execute();
//        $locationFollowers = $stmt->get_result();
//
//        $stmt->close();
//        return $locationFollowers;
//    }
    
      public function getCommentCount($postType, $id) {
          
          if($postType == "update") {
            $stmt = $this->conn->prepare("SELECT COUNT(*) AS count FROM update_commenteruser WHERE updateId = ? ");
            
            $stmt->bind_param("i", $id);
            $stmt->execute();
            $stmt->bind_result($count);
            $stmt->store_result();
            $stmt->fetch();
            $stmt->close();
        
//            echo "count of comments: " . $count;
            if($count != null)
                return $count;
            else
                return 0;
          }
          
          else if($postType == "post") {
            $stmt = $this->conn->prepare("SELECT COUNT(*) AS count FROM post_commenteruser WHERE postId = ? ");
            
            $stmt->bind_param("i", $id);
            $stmt->execute();
            $stmt->bind_result($count);
            $stmt->store_result();
            $stmt->fetch();
            $stmt->close();
        
//            echo "count of comments: " . $count;
            if($count != null)
                return $count;
            else
                return 0;
          }
    }
    
}
 
?>