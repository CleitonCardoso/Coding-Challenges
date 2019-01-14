import React, { Component } from 'react'
import { Link } from 'react-router-dom'
import {
    Button,
    Header,
    Image,
    Menu,
    Table,
    Modal,
    Form,
    Message,
    Popup
} from 'semantic-ui-react'

import {
    DateInput,
} from 'semantic-ui-calendar-react';

import 'semantic-ui-css/semantic.min.css'
import '../App.css'
import DealService from '../services/DealService'

const dealService = new DealService()

class Admin extends Component {

    constructor(props) {
        super(props);
        this.state = { selectedDeal: {} };
    }

    closeModal = () => {
        this.setState({
            openModal: false,
            selectedDeal: {
            }
        })
    }

    save = () => {
        if (this.state.hasOwnProperty('selectedDeal')) {
            dealService.save(this.state.selectedDeal, (response) => {
                this.closeModal()
                this.loadContent()
            }, (error) => {
                console.log(error)
            })
        }
    }

    openDeal = (deal, event) => {
        console.log(deal)
        this.setState({
            openModal: true,
            selectedDeal: deal
        })
    }

    newDeal = (event) => {
        this.setState({
            openModal: true,
            selectedDeal: {
            }
        })
    }

    loadContent = () => {
        dealService.listAll(response => {
            this.setState({ deals: response.data })
        }, () => { }, () => { })
    }

    componentWillMount = () => {
        this.loadContent()
    }

    updateSelectedDeal = (event, { name, value }) => {
        console.log(name, value);
        if (this.state.hasOwnProperty('selectedDeal')) {
            var selectedDeal = this.state.selectedDeal
            selectedDeal[name] = value
            this.setState({
                selectedDeal: selectedDeal
            })
        }
    }


    render() {
        return (
            <div className='container'>
                <header>
                    <div className='header-content'>
                        <Image src={require('../logo.svg')} floated='left' />
                        <Button primary floated='right' as={Link} to='/'>Ofertas</Button>
                    </div>
                </header>
                <main>
                    <Header size='medium' color='black' textAlign='center'> Ofertas cadastradas </Header>
                    <br />
                    <Menu borderless fluid>
                        <Menu.Item>
                            <Button primary onClick={this.newDeal.bind(this)}>Adicionar</Button>
                        </Menu.Item>
                    </Menu>
                    <div>
                        <Table striped selectable>
                            <Table.Header>
                                <Table.Row>
                                    <Table.HeaderCell>Título</Table.HeaderCell>
                                    <Table.HeaderCell>Data de criação</Table.HeaderCell>
                                    <Table.HeaderCell>Data de publicação</Table.HeaderCell>
                                    <Table.HeaderCell>Total vendidos</Table.HeaderCell>
                                </Table.Row>
                            </Table.Header>

                            <Table.Body>
                                {this.state.deals && this.state.deals.map((deal, index) =>
                                    <Popup trigger={
                                        <Table.Row onClick={this.openDeal.bind(this, deal)}>
                                            <Table.Cell>{deal.title}</Table.Cell>
                                            <Table.Cell>{deal.createDate}</Table.Cell>
                                            <Table.Cell>{deal.publishDate}</Table.Cell>
                                            <Table.Cell>{deal.totalSold}</Table.Cell>
                                        </Table.Row>
                                    } content='Clique para editar' key={index} />
                                )}
                            </Table.Body>
                        </Table>
                    </div>
                    <Modal open={this.state.openModal} style={{ width: '30%' }}>
                        <Modal.Header>{this.state.selectedDeal ? this.state.selectedDeal.title : 'Nova oferta'}</Modal.Header>
                        <Modal.Content >
                            <Message positive color='red' error hidden={!this.state.errorMessage}>
                                <Message.Header>Algo de errado aconteceu :(</Message.Header>
                                <p>
                                    {this.state.errorMessage}
                                </p>
                            </Message>
                            <Image wrapped size='small' src={require('../wireframe.png')} centered />
                            <Form unstackable>
                                <Form.Input
                                    name='title'
                                    label='Título da oferta'
                                    placeholder='Título da oferta'
                                    value={this.state.selectedDeal.title}
                                    onChange={this.updateSelectedDeal} />
                                <Form.Input
                                    name='text'
                                    label='Texto de destaque'
                                    placeholder='Texto de destaque'
                                    value={this.state.selectedDeal.text}
                                    onChange={this.updateSelectedDeal} />
                                <DateInput
                                    name='date'
                                    placeholder='Validade'
                                    value={this.state.selectedDeal.endDate ? this.state.selectedDeal.endDate : '00-00-0000'}
                                    iconPosition='left'
                                    onChange={this.updateSelectedDeal}
                                    dateFormat='DD-MM-YYYY'
                                    label='Dia limite da oferta'
                                />
                                <Form.Input
                                    name='url'
                                    label='URL da oferta'
                                    placeholder='URL'
                                    onChange={this.updateSelectedDeal} />
                                <Form.Input
                                    name='type'
                                    label='Tipo'
                                    placeholder='Tipo'
                                    onChange={this.updateSelectedDeal} />
                            </Form>
                        </Modal.Content>
                        <Modal.Actions>
                            <Button onClick={this.closeModal} negative>Cancelar</Button>
                            <Button onClick={this.save} positive>Salvar</Button>
                        </Modal.Actions>
                    </Modal>
                </main>
                <footer> <Header size='small' color='black'>Desenvolvido por <a href='http://cleitoncardoso.github.io/' target='_blank' rel='noopener noreferrer'>Cleiton Cardoso </a></Header></footer>
            </div>
        );
    }
}

export default Admin;
