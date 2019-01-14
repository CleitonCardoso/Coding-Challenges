import axios from 'axios'
import {
    API_ROOT
} from './api-config'

const serverUrl = API_ROOT


export default class DealService {

    save = (deal, successCallback, errorCallback) => {
        axios({
            method: 'post',
            url: serverUrl + '/deal',
            headers: {
                'Content-Type': 'application/json'
            },
            data: deal
        }).then(response => {
            successCallback(response)
        }).catch(error => {
            errorCallback(error)
        })
    }

    listAll = (successCallback, errorCallback) => {
        axios({
            method: 'get',
            url: serverUrl + '/deal',
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(response => {
            successCallback(response)
        }).catch(error => {
            errorCallback(error)
        })
    }

    confirmSale = (buyOptionId, successCallback, errorCallback) => {
        axios({
            method: 'post',
            url: serverUrl + '/deal/confirm-sale/' + buyOptionId,
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(response => {
            successCallback(response)
        }).catch(error => {
            errorCallback(error)
        })
    }
}