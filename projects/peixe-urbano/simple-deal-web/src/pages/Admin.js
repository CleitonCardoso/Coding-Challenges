import React, { Component } from 'react'
import { Link } from 'react-router-dom'
import {
    Button,
    Container,
    Grid,
    Header,
    Image,
    Item,
    Menu,
    Segment,
    Dropdown,
    Table,
    Modal
} from 'semantic-ui-react'
import 'semantic-ui-css/semantic.min.css'
import '../App.css'
import DealService from '../services/dealService'

const dealService = new DealService()

class Admin extends Component {
    state = {
    }

    cancel = () => {
        this.setState({
            openModal: false,
            selectedDeal: undefined,
        })
    }

    save = () => {
        console.log(this.state.selectedDeal)
    }

    openDeal = (deal, event) => {
        this.setState({
            openModal: true,
            selectedDeal: deal
        })
    }

    loadContent = () => {
        dealService.listAll(response => {
            this.setState({ deals: response.data })
        })
    }

    componentWillMount = () => {
        this.loadContent()
    }


    render() {
        return (
            <div className="container">
                <header>
                    <div className="header-content">
                        <Image src={require('../logo.svg')} floated='left' />
                        <Button primary floated='right' as={Link} to='/'>Ofertas</Button>
                    </div>
                </header>
                <main>
                    <Header size='medium' color='black' textAlign='center'> Ofertas cadastradas </Header>
                    <br />
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
                                    <Table.Row key={index} onClick={this.openDeal.bind(this, deal)}>
                                        <Table.Cell>{deal.title}</Table.Cell>
                                        <Table.Cell>{deal.createDate}</Table.Cell>
                                        <Table.Cell>{deal.publishDate}</Table.Cell>
                                        <Table.Cell>{deal.totalSold}</Table.Cell>
                                    </Table.Row>
                                )}
                            </Table.Body>
                        </Table>
                    </div>
                    <Modal open={this.state.openModal} centered>
                        <Modal.Header>{this.state.selectedDeal ? this.state.selectedDeal.title : undefined}</Modal.Header>
                        <Modal.Content>
                            <Image wrapped size='medium' src='https://react.semantic-ui.com/images/avatar/large/rachel.png' centered />
                            <Modal.Description>
                                <Header>Default Profile Image</Header>
                                <p>We've found the following gravatar image associated with your e-mail address.</p>
                                <p>Is it okay to use this photo?</p>
                            </Modal.Description>
                            <Modal.Actions>
                                <Button onClick={this.cancel} negative>Cancelar</Button>
                                <Button onClick={this.save} positive icon='checkmark' labelPosition='right' content='Yes' >Salvar</Button>
                            </Modal.Actions>
                        </Modal.Content>
                    </Modal>
                </main>
                <footer> <Header size='small' color='black'>Desenvolvido por <a href="http://cleitoncardoso.github.io/" target="_blank" rel="noopener noreferrer">Cleiton Cardoso </a></Header></footer>
            </div>
        );
    }
}

export default Admin;
