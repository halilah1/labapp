<?php
function is_xss_attack($query) {
    $xss_patterns = [
        '<script>', '</script>', 'javascript:', 'onload=', 'onerror=',
        'alert(', 'eval(', 'src=', 'href=', 'document.cookie', 'document.location'
    ];
    foreach ($xss_patterns as $pattern) {
        if (stripos($query, $pattern) !== false) {
            return true;
        }
    }
    return false;
}

function is_sql_injection($query) {
    $sql_patterns = [
        "'", '--', '/*', '*/', 'OR 1=1', 'UNION', 'SELECT', 'DROP', 'INSERT', 'UPDATE', 'DELETE',
        'exec', 'xp_', 'sp_', 'declare', 'cast', 'convert'
    ];
    foreach ($sql_patterns as $pattern) {
        if (stripos($query, $pattern) !== false) {
            return true;
        }
    }
    return false;
}

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $query = $_POST['query'];

    if (is_xss_attack($query)) {
        echo '<!doctype html>
        <html>
        <head>
            <title>Home Page</title>
        </head>
        <body>
            <p>Invalid input detected. Please try again.</p>
            <form method="post" action="dashboard.php">
                <input type="text" name="query" />
                <button type="submit">Submit</button>
            </form>
        </body>
        </html>';
        exit();
    }

    if (is_sql_injection($query)) {
        echo '<!doctype html>
        <html>
        <head>
            <title>Home Page</title>
        </head>
        <body>
            <p>Invalid input detected. Please try again.</p>
            <form method="post" action="dashboard.php">
                <input type="text" name="query" />
                <button type="submit">Submit</button>
            </form>
        </body>
        </html>';
        exit();
    }

    echo '<!doctype html>
    <html>
    <head>
        <title>Result Page</title>
    </head>
    <body>
        <p>Your search term: ' . htmlspecialchars($query) . '</p>
        <a href="index.php">Go back to home</a>
    </body>
    </html>';
}
?>
