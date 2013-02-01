from google.appengine.ext import db
import os
import random
import cgi
import sys
from databases import LanCloudUsers
method = os.environ['REQUEST_METHOD']

def user_valid(username,password):
    query = LanCloudUsers.all()
    query.order("-uid")
    query.filter("username =",username)
    query.filter("password =",password)
    for user in query.run(limit=1):
        return user.uid

formdata = cgi.FieldStorage()
username = formdata.getvalue('username')
password = formdata.getvalue('password')
if username != None and password != None:
    valid = user_valid(username,password)
    if valid == None:
        print 'Username or Password - Wrong.'
    else:
        print 'uid=' + str(valid)
else:
    print 'Username or Password - Left Blank.'
    
    
