from google.appengine.ext import db
import os
import random
import cgi
import sys
from databases import LanCloudFiles
method = os.environ['REQUEST_METHOD']
files = set()

if method == 'GET':
    
    uid = cgi.FieldStorage().getvalue('uid')
    if uid != None:
        query = LanCloudFiles.all()
        query.order("-fid")
        query.filter("uid =",int(uid))
        for afile in query.run():
            files.add(afile.filename)
        for afile in files:
            print afile
        
    
