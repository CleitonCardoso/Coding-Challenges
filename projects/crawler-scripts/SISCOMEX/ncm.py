import requests

text_file = open("output.csv", "w")

def request(index):
    headers = {'X-CSRF-Token': 'M0nEAWcFFGkT/So8fMWJXKyHUCCB0c0RIxZxdNNww4F09k9xdqgUE6S7Sy1lag2Z11QtaeKq1fI='}
    data = {
    }
    page = ('0' + str(index), str(index))[index >= 10]
    url = 'https://portalunico.siscomex.gov.br/classif/api/sumario/'+ page + '?dataInteresse=2020-01-01T23:00:00.000Z'
    response = requests.get(url , headers= headers , data = data, verify=False)
    if (response.status_code == 200):
        json = response.json()
        for filho in json['filhos']:
            text_file.write(filho['codigo'] + ';' + filho['nomeExtenso'] + '\n')

for index in range(1,98):
    request(index)

text_file.close()
