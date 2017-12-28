import React from 'react';
import AppBar from 'material-ui/AppBar';
import FlatButton from 'material-ui/FlatButton';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import RaisedButton from 'material-ui/RaisedButton';
import Paper from 'material-ui/Paper';
import axios from 'axios';
import Moment from 'moment';
import Cookies from 'universal-cookie';


import PDVDialog from './PDVDialog';
import LeftMenu from './LeftMenu';

import {
  Table,
  TableBody,
  TableHeader,
  TableHeaderColumn,
  TableRow,
  TableRowColumn,
} from 'material-ui/Table';

const cookies = new Cookies();

export default class PDVListView extends React.Component {
  constructor(props) {
    super(props)
    this.reloadList = this.reloadList.bind(this)
    this.removeItem = this.removeItem.bind(this)

    var credentials = cookies.get('credentials');

    this.state = {
      selected: [],
      pdvs: [],
      isLogged: !!credentials,
      credentials: credentials
    }
  }

  reloadList = () => {
    axios({
      method: 'get',
      url: 'http://localhost:8080/pdv',
      headers: {
        'Content-Type': 'application/json',
      },
      auth: {
        username: this.state.credentials.username,
        password: this.state.credentials.password
      }
    }).then(res => {
      if (res.status === 200) {
        this.setState({
          pdvs: res.data,
          selected: []
        })
      }
    }).catch(error => {
      console.log(error);
    });
  }

  componentDidMount() {
    if (!this.state || !this.state.isLogged) {
      this.props.history.push("/")
    } else {
      this.reloadList();
    }
  }

  handleRowSelection = (selectedRows) => {
    if (selectedRows === "none") {
      selectedRows = []
    }
    this.setState({
      selected: selectedRows
    })
  };

  getSelected = () => {
    return this.state.pdvs[this.state.selected];
  }

  addItem = (e) => {
    e.preventDefault();
    this.refs.myDialog.handleOpen(this.getSelected(), this.state.credentials);
  }

  removeItem = (e) => {
    e.preventDefault();
    if (this.state.selected.length > 0) {
      var id = this.state.pdvs[this.state.selected].id;
      axios({
        method: 'delete',
        url: 'http://localhost:8080/pdv/' + id,
        headers: {
          'Content-Type': 'application/json',
        },
        auth: {
          username: this.state.credentials.username,
          password: this.state.credentials.password
        }
      }).then(res => {
        if (res.status === 200) {
          this.reloadList();
        }
      });

      this.reloadList();
    }
  }

  logout = (e) => {
    e.preventDefault();
    cookies.remove('credentials');
    this.props.history.push('/', { state: {} });
  }

  render() {
    return (
      <div>
        <MuiThemeProvider>
          <section id="page">
            <PDVDialog parentContext={this} appContext={this.props.appContext} ref="myDialog" handler={this.reloadList} />
            <header>
              <AppBar title={<span >PDVs</span>}
                showMenuIconButton={false}
                iconElementRight={<FlatButton label="Sair" onClick={this.logout} />} />
            </header>
            <nav>
              <LeftMenu parentContext={this} appContext={this.props.appContext} />
            </nav>
            <main>
              <Paper zDepth={1} >
                <PDVDialog parentContext={this} appContext={this.props.appContext} ref="myDialog" handler={this.reloadList} />
                <div style={buttons}>
                  <br />
                  <RaisedButton label={this.state.selected.length === 0 ? "Adicionar" : "Editar"} primary style={btn} onClick={this.addItem} disabled={this.state.selected.length > 1} />
                  <RaisedButton label="Remover" secondary style={btn} disabled={this.state.selected.length === 0} onClick={this.removeItem} />
                  <br />
                </div>
                <Table onRowSelection={this.handleRowSelection} multiSelectable={false} setSelectedRows={this.state.selected}  >
                  <TableHeader enableSelectAll={true} displaySelectAll={true}  >
                    <TableRow>
                      <TableHeaderColumn>Nome</TableHeaderColumn>
                      <TableHeaderColumn>Endereço</TableHeaderColumn>
                      <TableHeaderColumn>Telefone</TableHeaderColumn>
                      <TableHeaderColumn>Horário início</TableHeaderColumn>
                      <TableHeaderColumn>Horário fim</TableHeaderColumn>
                    </TableRow>
                  </TableHeader>
                  <TableBody showRowHover={true} deselectOnClickaway={false}>
                    {
                      this.state.pdvs.map((pdv, index) =>
                        <TableRow key={index} selected={this.state.selected.indexOf(index) !== -1} >
                          <TableRowColumn>{pdv.name}</TableRowColumn>
                          <TableRowColumn>{pdv.address}</TableRowColumn>
                          <TableRowColumn>{pdv.phone}</TableRowColumn>
                          <TableRowColumn>{Moment(pdv.startingDate).format('HH:mm')}</TableRowColumn>
                          <TableRowColumn>{Moment(pdv.endingDate).format('HH:mm')}</TableRowColumn>
                        </TableRow>
                      )
                    }
                  </TableBody>
                </Table>
              </Paper>
            </main>
          </section>
        </MuiThemeProvider>
      </div >
    );
  }

}

const btn = {
  margin: 20
}

const buttons = {
  textAlign: 'left'
}
