from google.appengine.ext import db
import os
import random
import cgi
import sys
from databases import LanCloudFiles
method = os.environ['REQUEST_METHOD']

if method == 'GET':
    formdata = cgi.FieldStorage()
    uid = formdata.getvalue('uid')
    filename = formdata.getvalue('filename')

    if uid != None and filename != None:
        query = LanCloudFiles.all()
        query.order("-fid")
        query.filter("uid =",int(uid))
        query.filter("filename =",filename)
        query.order("-rev")

        for thefile in query.run(limit=1):
            print thefile.content
        
