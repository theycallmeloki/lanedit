from google.appengine.ext import db
import os
import random
import cgi
import sys
from databases import LanCloudFiles
method = os.environ['REQUEST_METHOD']

def new_fid():
    query = LanCloudFiles.all()
    query.order("-fid")
    get = 0;
    for last_fid in query.run(limit=1):
        get = last_fid.fid
    if get == 0:
        new_fid = 1
    else:
        new_fid = get + 1
    return new_fid

def new_rev(filename,uid):
    query = LanCloudFiles.all()
    query.order("-fid")
    query.filter("filename =",filename)
    query.filter("uid =",uid)
    rev = 0
    for onefile in query.run(limit=1):
        rev = onefile.rev
    if rev == 0:
        new_rev = 1
    else:
        new_rev = rev + 1
    return new_rev



if method == 'POST':
    formdata = cgi.FieldStorage()
    uid = int(formdata.getvalue('uid'))
    filename = formdata.getvalue('filename')
    content = formdata.getvalue('content')
    fid = new_fid()
    rev = new_rev(filename,uid)
    #fid,filename,content,rev,uid.


    if content != None:
        insert = LanCloudFiles()
        insert.fid = fid
        insert.filename = filename
        insert.content = content
        insert.rev = rev
        insert.uid = uid
        insert.put()

        print 'wrote ' + str(len(content)) + ' bytes'

    

    
    
    

    
