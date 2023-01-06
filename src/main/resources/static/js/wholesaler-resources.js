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
});
