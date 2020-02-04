import requests
import sys
import psycopg2
import os



text_file = open("output.txt", "w")

def nexoWebRequest(contador):
    headers = {}
    data = {
    
    }
    url = 'https://www.nexojornal.com.br/grafico/widget/?widgetContentId=561&widgetName=viewPicker&offset=' + `contador`  + '&elementwidth='
    print(url)
    req = requests.get( url , headers= headers , data = data)
    print(req.status_code)
    if (req.status_code == 200):
        text_file.write(req.content)


for contador in range(0,1000, 9):
    nexoWebRequest(contador)

text_file.close()




