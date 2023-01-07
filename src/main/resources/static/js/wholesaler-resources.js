'use strict';
var hrApp = angular.module('hrApp', ['ngRoute']);
hrApp.value('version', '1.0');


hrApp.config(function ($routeProvider, $locationProvider) {
    $routeProvider

        //NOTHING
        .when('/', {
            template: "",
        })
        // ROLES

        // CREATE
        .when('/roles/add', {
            templateUrl: '/view/role/role-form.html',
            controller: 'RoleController',
            resolve: {
                action: function () {
                    return 'add';
                },
            },
        })
        //READ-ALL
        .when('/roles/all', {
            templateUrl: '/view/role/roles.html',
            controller: 'RoleController',
            resolve: {
                action: function () {
                    return 'all';
                },
            },
        })
        //READ-ONE
        .when('/roles/one/:id', {
            templateUrl: '/view/role/role.html',
            controller: 'RoleController',
            resolve: {
                action: function () {
                    return 'one';
                },
            },
        })
        //UPDATE
        .when('/roles/update/:id', {
            templateUrl: '/view/role/role-form.html',
            controller: 'RoleController',
            resolve: {
                action: function () {
                    return 'update';
                },
            },
        })
        //DELETE
        .when('/roles/delete/:id', {
            template: "",
            controller: 'RoleController',
            resolve: {
                action: function () {
                    return 'delete';
                },
            },
        })
        //---------------------------------------
        // USER
        //---------------------------------------
        // CREATE
        .when('/users/add', {
            templateUrl: '/view/user/user-form.html',
            controller: 'UserController',
            resolve: {
                action: function () {
                    return 'add';
                },
            },
        })
        //READ-ALL
        .when('/users/all', {
            templateUrl: '/view/user/users.html',
            controller: 'UserController',
            resolve: {
                action: function () {
                    return 'all';
                },
            },
        })
        //READ-ONE
        .when('/users/one/:id', {
            templateUrl: '/view/user/user.html',
            controller: 'UserController',
            resolve: {
                action: function () {
                    return 'one';
                },
            },
        })
        //UPDATE
        .when('/users/update/:id', {
            templateUrl: '/view/user/user-form.html',
            controller: 'UserController',
            resolve: {
                action: function () {
                    return 'update';
                },
            },
        })
        //DELETE
        .when('/users/delete/:id', {
            template: "",
            controller: 'UserController',
            resolve: {
                action: function () {
                    return 'delete';
                },
            },
        })

        // INDUSTRY
        //---------------------------------------
        // CREATE
        .when('/industries/add', {
            templateUrl: '/view/industry/industry-form.html',
            controller: 'IndustryController',
            resolve: {
                action: function () {
                    return 'add';
                },
            },
        })
        //READ-ALL
        .when('/industries/all', {
            templateUrl: '/view/industry/industries.html',
            controller: 'IndustryController',
            resolve: {
                action: function () {
                    return 'all';
                },
            },
        })
        //READ-ONE
        .when('/industries/one/:id', {
            templateUrl: '/view/industry/industry.html',
            controller: 'IndustryController',
            resolve: {
                action: function () {
                    return 'one';
                },
            },
        })
        //UPDATE
        .when('/industries/update/:id', {
            templateUrl: '/view/industry/industry-form.html',
            controller: 'IndustryController',
            resolve: {
                action: function () {
                    return 'update';
                },
            },
        })
        //DELETE
        .when('/industries/delete/:id', {
            template: "",
            controller: 'IndustryController',
            resolve: {
                action: function () {
                    return 'delete';
                },
            },
        })

        // COMPANY
        //---------------------------------------
        // CREATE
        .when('/companies/add', {
            templateUrl: '/view/company/company-form.html',
            controller: 'CompanyController',
            resolve: {
                action: function () {
                    return 'add';
                },
            },
        })
        //READ-ALL
        .when('/companies/all', {
            templateUrl: '/view/company/companies.html',
            controller: 'CompanyController',
            resolve: {
                action: function () {
                    return 'all';
                },
            },
        })
        //READ-ONE
        .when('/companies/one/:id', {
            templateUrl: '/view/company/company.html',
            controller: 'CompanyController',
            resolve: {
                action: function () {
                    return 'one';
                },
            },
        })
        //UPDATE
        .when('/companies/update/:id', {
            templateUrl: '/view/company/company-form.html',
            controller: 'CompanyController',
            resolve: {
                action: function () {
                    return 'update';
                },
            },
        })
        //DELETE
        .when('/companies/delete/:id', {
            template: "",
            controller: 'CompanyController',
            resolve: {
                action: function () {
                    return 'delete';
                },
            },
        })

        // TRADE NOTE
        //---------------------------------------
        // CREATE
        .when('/tradeNotes/add', {
            templateUrl: '/view/tradenote/tradenote-form.html',
            controller: 'TradeNoteController',
            resolve: {
                action: function () {
                    return 'add';
                },
            },
        })
        //READ-ALL
        .when('/tradeNotes/all', {
            templateUrl: '/view/tradenote/tradenotes.html',
            controller: 'TradeNoteController',
            resolve: {
                action: function () {
                    return 'all';
                },
            },
        })
        //READ-ONE
        .when('/tradeNotes/one/:id', {
            templateUrl: '/view/tradenote/tradenote.html',
            controller: 'TradeNoteController',
            resolve: {
                action: function () {
                    return 'one';
                },
            },
        })
        //UPDATE
        .when('/tradeNotes/update/:id', {
            templateUrl: '/view/tradenote/tradenote-form.html',
            controller: 'TradeNoteController',
            resolve: {
                action: function () {
                    return 'update';
                },
            },
        })
        //DELETE
        .when('/tradeNotes/delete/:id', {
            template: "",
            controller: 'TradeNoteController',
            resolve: {
                action: function () {
                    return 'delete';
                },
            },
        })

        // CONTACT PERSON
        //---------------------------------------
        // CREATE
        .when('/contactPersons/add', {
            templateUrl: '/view/contactperson/contactperson-form.html',
            controller: 'ContactPersonController',
            resolve: {
                action: function () {
                    return 'add';
                },
            },
        })
        //READ-ALL
        .when('/contactPersons/all', {
            templateUrl: '/view/contactperson/contactpersons.html',
            controller: 'ContactPersonController',
            resolve: {
                action: function () {
                    return 'all';
                },
            },
        })
        //READ-ONE
        .when('/contactPersons/one/:id', {
            templateUrl: '/view/contactperson/contactperson.html',
            controller: 'ContactPersonController',
            resolve: {
                action: function () {
                    return 'one';
                },
            },
        })
        //UPDATE
        .when('/contactPersons/update/:id', {
            templateUrl: '/view/contactperson/contactperson-form.html',
            controller: 'ContactPersonController',
            resolve: {
                action: function () {
                    return 'update';
                },
            },
        })
        //DELETE
        .when('/contactPersons/delete/:id', {
            template: "",
            controller: 'ContactPersonController',
            resolve: {
                action: function () {
                    return 'delete';
                },
            },
        })


});
