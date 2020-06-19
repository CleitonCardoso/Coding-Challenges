import requests

text_file = open("output.csv", "w")

def request(index):
    headers = {'X-CSRF-Token': 'Xsy80i/1iL0suSNpZAnsM4RvuNw6VPAzphdgVQqK6efgQo5xWWkJ/RgywAfxGlcmbJA4zsc53lI='}
    data = {
    }
    page = ('0' + str(index), str(index))[index >= 10]
    url = 'https://portalunico.siscomex.gov.br/classif/api/nomenclatura/'+ page + '?dataInteresse=2020-06-19T16:44:50.000Z'
    response = requests.get(url , headers= headers , data = data, verify=False)
    print(response.status_code)
    if (response.status_code == 200):
        json = response.json()
        for filho in json['filhos']:
            text_file.write(filho['codigo'] + ';' + filho['nomeExtenso'] + '\n')

for index in range(1,98):
    request(index)

text_file.close()
