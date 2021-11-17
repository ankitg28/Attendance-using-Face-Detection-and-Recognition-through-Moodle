<?php

define('AUTH_PASSWORD_NOT_CACHED', 'not cached');

echo user_login('admin','Divyank@123');
function password_is_legacy_hash($password) {
    return (bool) preg_match('/^[0-9a-f]{32}$/', $password);
}

function user_login($username, $password) {
        global $CFG, $DB, $USER;
        if (!$user = $DB->get_record('user', array('username'=>$username, 'mnethostid'=>$CFG->mnet_localhost_id))) {
            return false;
        }
        if (!validate_internal_user_password($user, $password)) {
            return false;
        }
        if ($password === 'changeme') {
            // force the change - this is deprecated and it makes sense only for manual auth,
            // because most other plugins can not change password easily or
            // passwords are always specified by users
            set_user_preference('auth_forcepasswordchange', true, $user->id);
        }
        return true;
}

function validate_internal_user_password($user, $password) {
    global $CFG;

    if ($user->password === AUTH_PASSWORD_NOT_CACHED) {
        // Internal password is not used at all, it can not validate.
        return false;
    }
    // If hash isn't a legacy (md5) hash, validate using the library function.
    if (!password_is_legacy_hash($user->password)) {
        return password_verify($password, $user->password);
    }

    $sitesalt = isset($CFG->passwordsaltmain) ? $CFG->passwordsaltmain : '';
    $validated = false;

    if ($user->password === md5($password.$sitesalt)
            or $user->password === md5($password)
            or $user->password === md5(addslashes($password).$sitesalt)
            or $user->password === md5(addslashes($password))) {
        // Note: we are intentionally using the addslashes() here because we
        //       need to accept old password hashes of passwords with magic quotes.
        $validated = true;

    } else {
        for ($i=1; $i<=20; $i++) { // 20 alternative salts should be enough, right?
            $alt = 'passwordsaltalt'.$i;
            if (!empty($CFG->$alt)) {
                if ($user->password === md5($password.$CFG->$alt) or $user->password === md5(addslashes($password).$CFG->$alt)) {
                    $validated = true;
                    break;
                }
            }
        }
    }

    if ($validated) {
        // If the password matches the existing md5 hash, update to the
        // current hash algorithm while we have access to the user's password.
        update_internal_user_password($user, $password);
    }

    return $validated;
}