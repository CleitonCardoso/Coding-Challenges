import React, { Component } from 'react';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import AppBar from 'material-ui/AppBar';
import RaisedButton from 'material-ui/RaisedButton';
import TextField from 'material-ui/TextField';
import Paper from 'material-ui/Paper';
import axios from 'axios';
import PropTypes from 'prop-types';
import Cookies from 'universal-cookie';

const cookies = new Cookies();

class Login extends Component {

  constructor(props) {
    super(props);
    var credentials = cookies.get('credentials');

    this.state = {
      isLogged: !!credentials,
      credentials: credentials
    }
  }

  login = (e) => {
    e.preventDefault();
    let data = 'username=' + this.state.username + '&password=' + this.state.password;

    axios({
      method: 'post',
      url: 'http://localhost:8080/login',
      data: data,
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      }
    }).then(res => {
      console.log(res);
      cookies.set('credentials', {
        username: this.state.username,
        password: this.state.password
      }, { path: '/' });
      this.props.history.push('/pdvs');
    }).catch(error => {
      console.log(error);
    });
  }


  render() {
    return (
      <MuiThemeProvider>
        <div className="login">
          <AppBar title="Sisteminha" showMenuIconButton={false} />
          <div style={loginBox}>
            <Paper zDepth={1} >
              <TextField hintText="Admin" floatingLabelText="Usuário" onChange={(event, newValue) => this.setState({ username: newValue })} />
              <br />
              <TextField type="password" hintText="Admin" floatingLabelText="Senha" onChange={(event, newValue) => this.setState({ password: newValue })} />
              <br />
              <RaisedButton label="Entrar" primary={true} style={style} onClick={this.login} />
            </Paper>
          </div>
        </div>
      </MuiThemeProvider>
    );
  }
}

Login.contextTypes = {
  credentials: PropTypes.object
};

const loginBox = {
  width: 500,
  margin: '100px auto'
};

const style = {
  margin: 15,
};

export default Login;