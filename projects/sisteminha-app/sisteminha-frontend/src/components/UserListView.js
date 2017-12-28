import React from 'react';
import AppBar from 'material-ui/AppBar';
import FlatButton from 'material-ui/FlatButton';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import RaisedButton from 'material-ui/RaisedButton';
import Paper from 'material-ui/Paper';
import axios from 'axios';
import Cookies from 'universal-cookie';

import './App.css';

import UserDialog from './UserDialog';
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

export default class UserListView extends React.Component {
  constructor(props) {
    super(props)
    this.reloadList = this.reloadList.bind(this)
    this.removeItem = this.removeItem.bind(this)

    var credentials = cookies.get('credentials');

    this.state = {
      selected: [],
      users: [],
      isLogged: !!credentials,
      credentials: credentials
    }
  }

  reloadList = () => {
    axios({
      method: 'get',
      url: 'http://localhost:8080/user',
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
          users: res.data,
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
    return this.state.users[this.state.selected];
  }

  addItem = (e) => {
    e.preventDefault();
    this.refs.myDialog.handleOpen(this.getSelected(), this.state.credentials);
  }

  removeItem = (e) => {
    e.preventDefault();
    if (this.state.selected.length > 0) {
      var id = this.state.users[this.state.selected].id;
      axios({
        method: 'delete',
        url: 'http://localhost:8080/user/' + id,
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
            <UserDialog parentContext={this} appContext={this.props.appContext} ref="myDialog" handler={this.reloadList} />
            <header>
              <AppBar title={<span >Usuários</span>}
                showMenuIconButton={false}
                iconElementRight={<FlatButton label="Sair" onClick={this.logout} />} />
            </header>
            <nav>
              <LeftMenu parentContext={this} appContext={this.props.appContext} />
            </nav>
            <main>
              <Paper zDepth={1} >
                <div style={buttons}>
                  <br />
                  <RaisedButton label={this.state.selected.length === 0 ? "Adicionar" : "Editar"} primary style={btn} onClick={this.addItem} disabled={this.state.selected.length > 1} />
                  <RaisedButton label="Remover" secondary style={btn} disabled={this.state.selected.length === 0} onClick={this.removeItem} />
                  <br />
                </div>
                <Table onRowSelection={this.handleRowSelection} setSelectedRows={this.state.selected}  >
                  <TableHeader enableSelectAll={true} displaySelectAll={true}  >
                    <TableRow>
                      <TableHeaderColumn>Nome de usuário</TableHeaderColumn>
                      <TableHeaderColumn>Status</TableHeaderColumn>
                    </TableRow>
                  </TableHeader>
                  <TableBody showRowHover={true} deselectOnClickaway={false}>
                    {
                      this.state.users.map((user, index) =>
                        <TableRow key={index} selected={this.state.selected.indexOf(index) !== -1} >
                          <TableRowColumn>{user.username}</TableRowColumn>
                          <TableRowColumn>{user.active ? "Ativo" : "Inativo"}</TableRowColumn>
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
