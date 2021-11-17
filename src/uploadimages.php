<?php

$upload_path = 'uploads/';

$server_ip = gethostbyname(gethostname());

$upload_url = 'http://localhost/' . $upload_path;

$response = array();

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    
    if (isset($_POST['name']) and isset($_FILES['image']['name'])) {
        
        $name = $_POST['name'];
        
        $fileinfo = pathinfo($_FILES['image']['name']);
        
        $extension = $fileinfo['extension'];
        
        $file_url = $upload_url . $name . '.' . $extension;
        
        $file_path = $upload_path . $name . '.' . $extension;
        
        try {
            move_uploaded_file($_FILES['image']['tmp_name'], $file_path);
            $response['error'] = false;
            $response['url']   = $file_url;
            $response['name']  = $name;
        }
        catch (Exception $e) {
            $response['error']   = true;
            $response['message'] = $e->getMessage();
        }
        echo json_encode($response);
    } else {
        $response['error']   = true;
        $response['message'] = 'Please choose a file';
    }
}