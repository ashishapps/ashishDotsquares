/*
    This file is generated and updated by Sencha Cmd. You can edit this file as
    needed for your application, but these edits will have to be merged by
    Sencha Cmd when it performs code generation tasks such as generating new
    models, controllers or views and when running "sencha app upgrade".

    Ideally changes to this file would be limited and most work would be done
    in other places (such as Controllers). If Sencha Cmd cannot merge your
    changes and its generated code, it will produce a "merge conflict" that you
    will need to resolve manually.
*/
Ext.Loader.setConfig({ 
 enabled: true
});
 
Ext.application({
    name: 'CommunityApp',

    requires: [
        'Ext.MessageBox',
    ],

    views: [
        'Main',
        'Login',
		'Register',
		'Home',
		'Faith',
		'HomeBase'
    ],
	controllers:[
		'Login',
		'Home',
		'Register',
		'faith'
	],
	models:['login','country','state','faith','comment'],
	stores:['login','country','state','faith','comment'],

launch: function() {
            //Destroy the #appLoadingIndicator element
	        u_type=localStorage.getItem("u_type");
			var login=localStorage.getItem("email");
			var password=localStorage.getItem("password");
			Ext.Ajax.request({
				url : global_url,
				method : 'POST',
				callbackKey: 'callback',
					params : {
					json_string : Ext.encode({
								 "auth_key": "abbad35c5c01-xxxx-xxx",
								 "action": "login",
								 "email"	: login,
								 "password":password
									})
				},
				success : function(response, data) {
				 	var result=Ext.decode(response.responseText);
				 	if(result.data.status==1){
				 		//debugger
				 		 Ext.Viewport.add(Ext.create('CommunityApp.view.Main'));
				 		 Ext.getCmp('Main').setActiveItem(2);
				 		 var faithcountlcl=localStorage.getItem("faithcount");
				 		servercount=result.data.counts.faiths;
				 		console.log(servercount+faithcountlcl)
				 		 if(faithcountlcl==servercount ){
				 			Ext.getCmp('btnfaith').setBadgeText(0);
				 		 }else if(servercount > faithcountlcl)
				 		{
				 			console.log(servercount+faithcountlcl)
				 			var cl=servercount-faithcountlcl;
				 			Ext.getCmp('btnfaith').setBadgeText(cl);
				 		}else{
				 			Ext.getCmp('btnfaith').setBadgeText(0);
				 		}
				 		Ext.fly('splash').destroy();
				 	}else{
				 		Ext.Viewport.add(Ext.create('CommunityApp.view.Main'));
				 		Ext.fly('splash').destroy();
				 	}
				},
				failure : function(response, opts) {
					Ext.Viewport.setMasked(false);
				}
			});
		
		// Initialize the main view
       
    },

    onUpdated: function() {
        Ext.Msg.confirm(
            "Application Update",
            "This application has just successfully been updated to the latest version. Reload now?",
            function(buttonId) {
                if (buttonId === 'yes') {
               //     window.location.reload();
                }
            }
        );
    }
});
