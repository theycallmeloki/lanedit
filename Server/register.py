from google.appengine.ext import db
import os
import random
import cgi
import sys
from databases import LanCloudUsers
method = os.environ['REQUEST_METHOD']

def new_uid():
    query = LanCloudUsers.all()
    query.order("-uid")
    get = 0
    for last_uid in query.run(limit=1):
        get = last_uid.uid
    if get == 0:
        new_uid = 1
    else:
        new_uid = get + 1
    return new_uid
        

if method == 'GET':
    username_exists = False
    exists = cgi.FieldStorage().getvalue('exists')
    password_same = False
    pwdsame = cgi.FieldStorage().getvalue('pwdsame')
    if exists == 'true':
        username_exists = True
    if pwdsame == 'true':
        password_same = True
        
    #depeding on username_exists, print out whether 
    #username exists or not.
    #same thing for password_same
        

    print """
<link rel="stylesheet" type="text/css" href="style.css">
<h1>Register</h1></br>
    """

    if username_exists == True:
        print 'Username already exists</br>'

    if password_same == True:
        print 'Both the passwords are not the same</br>'
    
    print """
<form action="/register" method="post">
<table>
<tr>
<td>Username</td><td><input type="text" name="username"/></td>
</tr>
<tr>
<td>Password</td><td><input type="password" name="password" /></td>
</tr>
<tr>
<td>Re-Enter Password</td><td><input type="password" name="reenter" /></td>
</tr>
<tr>
<td><input type="submit" value="Submit" /></td>
</tr>

</table>
</form>
"""

elif method == 'POST':
    formdata = cgi.FieldStorage()
    username = formdata.getvalue('username')
    password = formdata.getvalue('password')
    reenter = formdata.getvalue('reenter')
    

    if reenter != password:
        print 'Status: 302 Temporarily moved'
        print 'Location: /register?pwdsame=true'

    present = False
    query = LanCloudUsers.all()
    for user in query.run():
        if user.username == username:
            present = True

    if reenter == password:
        if present == True :
            print 'Status: 302 Temporarily moved'
            print 'Location: /register?exists=true'

    if reenter == password and present == False:
        insert = LanCloudUsers()
        insert.uid = new_uid()
        insert.username = username
        insert.password = password
        insert.put()

        print """
<link rel="stylesheet" type="text/css" href="style.css">
<p>New user """ + username + """ has been registered.</p>
<p>Click <a href="/lancloud">here</a> to go back and get the client.</p>
"""

    

    
    

