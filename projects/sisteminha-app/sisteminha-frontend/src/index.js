import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter, Route } from 'react-router-dom';
import './index.css'

import registerServiceWorker from './registerServiceWorker';
import App from './components/App';
import PDVListView from './components/PDVListView';
import UserListView from './components/UserListView';
import Login from './components/Login';


ReactDOM.render(
  <BrowserRouter>
    <div>
      <Route exact path="/" component={App} />
      <Route path="/login" component={Login} />
      <Route path="/pdvs" component={PDVListView} />
      <Route path="/users" component={UserListView} />
    </div>
  </BrowserRouter>
  , document.getElementById('root'));
registerServiceWorker();
