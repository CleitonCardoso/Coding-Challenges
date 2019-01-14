import axios from 'axios'
import {
    API_ROOT
} from './api-config'

const serverUrl = API_ROOT


export default class DealService {
    listAll = callback => {
        axios({
            method: 'get',
            url: serverUrl + '/deal',
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(response => {
            callback(response)
        }).catch(error => {
            console.log(error)
        })
    }

    confirmSale = (buyOptionId, callback) => {
        axios({
            method: 'post',
            url: serverUrl + '/deal/confirm-sale/' + buyOptionId,
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(response => {
            callback()
        }).catch(error => {
            console.log(error)
        })
    }
}