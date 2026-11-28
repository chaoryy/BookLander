<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Registration</title>
</head>
<body>
    <div>
        <h1>Registration</h1>
        <#if error??>
            <div> ${error}</div>
        </#if>

        <form action="/register" method="post">
            <div>
                <label for="name">First name:</label>
                <input type="text" id="name" name="name" required>
            </div>
            <div>
                <label for="lastName">Last name:</label>
                <input type="text" id="lastName" name="lastName" required>
            </div>
            <div>
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" required>
            </div>
            <div>
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required>
            </div>
            <div>
                <label for="role">Role:</label>
                <select id="role" name="role" required>
                    <#if roles??>
                        <#list roles as role>
                            <option value="${role}">${role.roleName}</option>
                        </#list>
                    <#else>
                        <option value="USER">User</option>
                        <option value="ADMIN">Admin</option>
                    </#if>
                </select>
            </div>
            <button type="submit">Register</button>
        </form>
        <a href="/login">Already have an account? Log in</a>
    </div>
</body>
</html>