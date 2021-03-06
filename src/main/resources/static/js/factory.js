angular.module('son')
    .factory('colors', ['APP_COLORS', function (colors) {

        return {
            byName: function (name) {
                return (colors[name] || '#fff');
            }
        };

    }])

    .factory('UsuarioService', ['APP_END_POINT', '$resource', function (APP_END_POINT, $resource) {

        return $resource(APP_END_POINT + '/api/admin/usuarios', null,
            {
                'get': {method: 'GET'},
                'save': {method: 'POST'},
                'query': {method: 'GET', isArray: true},
                'remove': {method: 'DELETE'},
                'delete': {method: 'DELETE'}
            }
        );

    }])
    .factory('AuthService', ['APP_END_POINT', '$http', '$localStorage', '$sessionStorage', '$location', '$rootScope', '$state', 'APP_MENUS', 'Upload',
        function (APP_END_POINT, $http, $localStorage, $sessionStorage, $location, $rootScope, $state, APP_MENUS, Upload) {

            var authService = {};


            authService.atualizaProfileUsuario = function (usuario, file) {
                return Upload.upload({
                    url: APP_END_POINT + '/api/admin/usuarios/upload',
                    data: {'file': file, 'usuario': usuario}
                });

            };

            authService.setUsuarioProfile = function (usuarioProfile) {
                return $http.put(APP_END_POINT + '/api/admin/usuarios/atualiza', usuarioProfile);
            };

            authService.login = function (credentials) {
                //return $http.post(APP_END_POINT + '/api/admin/autenticacao/auth', credentials);

                return $http.post(APP_END_POINT + '/api/admin/autenticacao/auth', credentials);
            };


            authService.formataUsuario = function (usuarioAFormatar) {

                var usuarioSession = {
                    login: usuarioAFormatar.login,
                    password: usuarioAFormatar.password,
                    name: usuarioAFormatar.name,
                    email: usuarioAFormatar.email,
                    erros: usuarioAFormatar.erros,
                    roles: [],
                    accountNonExpired: usuarioAFormatar.accountNonExpired,
                    accountNonLocked: usuarioAFormatar.accountNonLocked,
                    credentialsNonExpired: usuarioAFormatar.credentialsNonExpired,
                    enabled: usuarioAFormatar.enabled,
                    img: usuarioAFormatar.img,
                    picture: './img/profile/' + usuarioAFormatar.login + '.jpg'
                };

                for (var i = 0; i < usuarioAFormatar.roles.length; i++) {
                    usuarioSession.roles.push(usuarioAFormatar.roles[i].authority);
                }

                return usuarioSession;
            };

            authService.setUserSessionStorage = function (user) {
                user.block = false;
                $sessionStorage.user = user;
            };

            authService.setUserLocalStorage = function (user) {
                $localStorage.user = user;
            };


            authService.getUserSessionStorage = function () {
                return $sessionStorage.user;
            };

            authService.getUserLocalStorage = function () {
                return $localStorage.user;
            };


            authService.isAuth = function () {
                if (typeof($sessionStorage.user) != "undefined" && $sessionStorage.user != null && typeof($sessionStorage.user.login) != undefined && $sessionStorage.user.login != null) {
                    return true;
                } else {
                    return false;
                }
            };


            authService.logout = function () {
                delete $sessionStorage.user;
                delete $localStorage.user;
            };

            authService.recoverySenhaEmail = function (emailStr) {
                return $http.post(APP_END_POINT + '/api/admin/usuarios/recovery', {email: emailStr});
            };


            /*
             Metodos de controles
             */


            function getUserFromToken() {
                var token = $localStorage.token;
                var user = {};
                if (typeof token !== 'undefined') {
                    var encoded = token.split('.')[1];
                    user = JSON.parse(urlBase64Decode(encoded));
                }
                return user;
            };


            function changeUser(user) {
                angular.extend(currentUser, user);
            };

            function urlBase64Decode(str) {
                var output = str.replace('-', '+').replace('_', '/');
                switch (output.length % 4) {
                    case 0:
                        break;
                    case 2:
                        output += '==';
                        break;
                    case 3:
                        output += '=';
                        break;
                    default:
                        throw 'Cadeia de caracteres base64url inválida!';
                }
                return window.atob(output);
            };


            return authService;

        }])
    .factory('PessoaService', ['APP_END_POINT', '$http', 'APP_CEP_END_POINT',
        function (APP_END_POINT, $http, APP_CEP_END_POINT) {

            var pessoaService = {};

            pessoaService.validaCGC = function (cgc) {
                ////console.log('entrou no valida cgc');
                return $http.get(APP_END_POINT + '/pessoa/validacgc/' + cgc);
            };

            pessoaService.buscaCEPNet = function (cep) {
                ////console.log('entrou no valida cgc');
                return $http.get(APP_CEP_END_POINT + cep + '/json/');
            };

            pessoaService.deletePessoa = function (id) {
                return $http.delete(APP_END_POINT + '/pessoa/' + id);
            };

            pessoaService.postPessoa = function (pessoa) {
                if (pessoa._id > 0) {
                    return $http.put(APP_END_POINT + '/pessoa', pessoa);
                } else {
                    return $http.post(APP_END_POINT + '/pessoa', pessoa);
                }
            };

            pessoaService.getPessoas = function (pessoa) {
                return $http.post(APP_END_POINT + '/pessoas', pessoa);
            };

            pessoaService.getURLRemota = function () {
                return APP_END_POINT + '/pessoas/autocomplete/'
            };

            pessoaService.getPessoasURLAsync = function (des) {

                return $http.get(APP_END_POINT + '/pessoas/urlasync/' + des).then(function (response) {
                    return response.data.data.map(function (pessoa) {
                        return pessoa.nome;
                    });
                });


            };

            return pessoaService;

        }])
    .factory('ItemService', ['APP_END_POINT', '$http',
        function (APP_END_POINT, $http) {

            var ItemService = {};

            ItemService.setUnidade = function (unidade) {
                if (unidade._id > -1) {
                    return $http.put(APP_END_POINT + '/unidade', unidade);
                } else {
                    return $http.post(APP_END_POINT + '/unidade', unidade);
                }
            };

            ItemService.getUnidades = function () {
                return $http.get(APP_END_POINT + '/unidades');
            };

            ItemService.getUnidadesVasilhames = function () {
                return $http.get(APP_END_POINT + '/unidades/vasilhame');
            };

            ItemService.delUnidade = function (id) {
                return $http.delete(APP_END_POINT + '/unidade/' + id)
            };

            ItemService.getQtdVasilhameCodUnidade = function (codUnidade) {
                return $http.get(APP_END_POINT + '/unidade/vasilhame/' + codUnidade);
            };

            ItemService.postVasilhames = function (v) {
                return $http.post(APP_END_POINT + '/unidade/vasilhames', {vasilhames: v});
            };

            ItemService.getPosicaoVasilhames = function (v) {
                return $http.get(APP_END_POINT + '/unidades/vasilhames');
            };

            /////////////

            ItemService.setRua = function (rua) {
                if (rua._id > -1) {
                    return $http.put(APP_END_POINT + '/rua', rua);
                } else {
                    return $http.post(APP_END_POINT + '/rua', rua);
                }
            };

            ItemService.getRuas = function () {
                return $http.get(APP_END_POINT + '/ruas');
            };

            ItemService.delRua = function (id) {
                return $http.delete(APP_END_POINT + '/rua/' + id)
            };


            ////////////

            ItemService.setItem = function (item, flgItemPesquisado) {
                if (flgItemPesquisado != 0) {
                    return $http.put(APP_END_POINT + '/item', item);
                } else {
                    return $http.post(APP_END_POINT + '/item', item);
                }
            };

            ItemService.getItens = function (item) {
                return $http.post(APP_END_POINT + '/itens', item);
            };

            ItemService.delItem = function (id) {
                return $http.delete(APP_END_POINT + '/item/' + id);
            };

            return ItemService;

        }])
    .factory('VeiculoService', ['APP_END_POINT', '$http',
        function (APP_END_POINT, $http) {

            var veiculoService = {};

            veiculoService.setVeiculo = function (veiculo, flgItemPesquisado) {
                if (flgItemPesquisado != 0) {
                    //console.log('foi no put');
                    return $http.put(APP_END_POINT + '/veiculo', veiculo);
                } else {
                    //console.log('foi no post');
                    return $http.post(APP_END_POINT + '/veiculo', veiculo);
                }
            };

            veiculoService.delVeiculo = function (id) {
                return $http.delete(APP_END_POINT + '/veiculo/' + id);
            };

            veiculoService.getVeiculoID = function (id) {
                return $http.get(APP_END_POINT + '/veiculo/id/' + id);
            };

            veiculoService.findVeiculos = function (veiculo) {
                //console.log('factory veiculo ' + JSON.stringify(veiculo));
                return $http.post(APP_END_POINT + '/veiculos', veiculo);
            };

            return veiculoService;

        }])
    .factory('MovimentoService', ['APP_END_POINT', '$http',
        function (APP_END_POINT, $http) {

            var movimentoService = {};

            movimentoService.getVeiculosURLAsyncService = function (des) {
                // //console.log('executando o metodo');
                return $http.get(APP_END_POINT + '/veiculos/urlasync/' + des).then(function (response) {
                    var resposta = response.data;
                    if (resposta.type == true) {
                        return response.data.data;
                    } else {
                        return null;
                    }

                });


            };

            movimentoService.getMotoristasURLAsyncService = function (des) {
                // //console.log('executando o metodo');
                return $http.get(APP_END_POINT + '/pessoas/funcionarios/motoristas/urlasync/' + des).then(function (response) {
                    var resposta = response.data;
                    if (resposta.type == true) {
                        return response.data.data;
                    } else {
                        return null;
                    }

                });
            };

            movimentoService.getConferentesURLAsyncService = function (des) {
                // //console.log('executando o metodo');
                return $http.get(APP_END_POINT + '/pessoas/funcionarios/conferentes/urlasync/' + des).then(function (response) {
                    var resposta = response.data;
                    if (resposta.type == true) {
                        return response.data.data;
                    } else {
                        return null;
                    }

                });
            };

            movimentoService.getPessoaClienteURLAsyncService = function (des) {
                return $http.get(APP_END_POINT + '/pessoas/clientes/urlasync/' + des).then(function (response) {
                    var resposta = response.data;
                    if (resposta.type == true) {
                        return response.data.data;
                    } else {
                        return null;
                    }

                });
            };
            movimentoService.getPessoaForcedorURLAsyncService = function (des) {
                return $http.get(APP_END_POINT + '/pessoas/fornecedores/urlasync/' + des).then(function (response) {
                    var resposta = response.data;
                    if (resposta.type == true) {
                        return response.data.data;
                    } else {
                        return null;
                    }

                });
            };
            movimentoService.getItensURLAsyncService = function (des) {
                // //console.log('executando o metodo');
                return $http.get(APP_END_POINT + '/itens/urlasync/' + des).then(function (response) {
                    var resposta = response.data;
                    if (resposta.type == true) {
                        return response.data.data;
                    } else {
                        return null;
                    }

                });


            };

            movimentoService.setMovimento = function (movimento, flgItemPesquisado) {
                if (flgItemPesquisado != 0) {
                    console.log('foi no put' + JSON.stringify(movimento));
                    return $http.put(APP_END_POINT + '/movimento', movimento);
                } else {
                    //console.log('foi no post'+JSON.stringify(movimento));
                    return $http.post(APP_END_POINT + '/movimento', movimento);
                }
            };

            movimentoService.getMovimentos = function (movimento) {
                return $http.post(APP_END_POINT + '/movimentos', movimento);
            };


            return movimentoService;

        }])
    .factory('MotoristaService', ['APP_END_POINT', '$http',
        function (APP_END_POINT, $http) {


            var motoristaService = {};

            motoristaService.setMotorista = function (motorista, flgItemPesquisado) {
                if (flgItemPesquisado != 0) {
                    //console.log('foi no put');
                    return $http.put(APP_END_POINT + '/motorista', motorista);
                } else {
                    //console.log('foi no post');
                    return $http.post(APP_END_POINT + '/motorista', motorista);
                }
            };

            motoristaService.delMotorista = function (id) {
                return $http.delete(APP_END_POINT + '/motorista/' + id);
            };


            motoristaService.findMotoristas = function (motorista) {
                //console.log('factory veiculo ' + JSON.stringify(motorista));
                return $http.post(APP_END_POINT + '/motoristas', motorista);
            };

            return motoristaService;
        }])
    .factory('RelatorioService', ['APP_END_POINT', '$http',
        function (APP_END_POINT, $http) {


            var relatorioService = {};


            relatorioService.getPosicaoItem = function () {
                return $http.get(APP_END_POINT + '/relatorio/posicao/item');
            };

            relatorioService.getPosicaoItemUnidadeId = function (findItem) {
                return $http.post(APP_END_POINT + '/relatorio/posicao/item/unidade', findItem);
            };

            relatorioService.getPosicaoItemIds = function (ids) {
                return $http.post(APP_END_POINT + '/relatorio/posicao/item/ids', {ids: ids});
            };

            relatorioService.getPosicaoItemId = function (id) {
                return $http.get(APP_END_POINT + '/relatorio/posicao/item/' + id);
            };

            relatorioService.getAuditoriaItemId = function (id) {
                return $http.get(APP_END_POINT + '/relatorio/auditoria/item/' + id);
            };

            relatorioService.getInventario = function (inventario) {
                return $http.post(APP_END_POINT + '/relatorio/inventario/all', inventario);
            };

            return relatorioService;
        }])
    .factory('timesInterceptor', [
        function () {

            return {

                request: function (config) {
                    /*
                     var url = config.url;

                     if(url.indexOf(".html") > -1) return config;

                     var timestamp= new Date().getTime();
                     config.url =  url +"?timestamp="+timestamp;
                     */
                    return config;
                }
            }
        }])
    .factory('ConferenciaService', ['APP_END_POINT', '$http',
        function (APP_END_POINT, $http) {


            var conferenciaService = {};


            conferenciaService.getItens = function () {
                return $http.get(APP_END_POINT + '/itens/urlasync/all');
            };

            conferenciaService.getItemAsync = function (des) {
                return $http.get(APP_END_POINT + '/itens/urlasync/' + des).then(function (response) {
                    var resposta = response.data;
                    if (resposta.type == true) {
                        return response.data.data;
                    } else {
                        return null;
                    }

                });
            };

            conferenciaService.postConferencia = function (itensFuncao) {
                console.log(' post itens ' + JSON.stringify(itensFuncao));

                return $http.post(APP_END_POINT + '/itens/conferencia', itensFuncao);
            };

            conferenciaService.getConferencias = function (conferenciaBusca) {
                return $http.post(APP_END_POINT + '/conferencias', conferenciaBusca);
            };

            return conferenciaService;
        }])
;
