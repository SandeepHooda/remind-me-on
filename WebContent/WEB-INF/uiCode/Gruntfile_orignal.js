//npm install grunt
//npm install --save-dev grunt-sass

module.exports = function (grunt) {
    'use strict';
		grunt.initConfig({
			
			js_src_path:".",
			js_build_path:"../../MyShop",
			curretDate: "<%= grunt.template.today('yyyymmddhh') %>"+Math.random(),
			js_main_app: "js/prod<%= curretDate %>.js",
			
			
			
			clean:{
				cleanBefore:{
					options:{force:true},
					src:['<%= js_build_path %>']
				},
				cleanAfter:{
					options:{force:true},
					src:['<%= js_build_path %>/ui','<%= js_src_path %>/ui' ]
				}
				
			},

			concat:{
				options:{
					separator:';'
				},
				js:{
					src:[ '<%= js_src_path %>/js/app.js', '<%= js_src_path %>/add/**/*.js', '<%= js_src_path %>/admin/**/*.js','<%= js_src_path %>/address/**/*.js','<%= js_src_path %>/cart/**/*.js','<%= js_src_path %>/checkout/**/*.js','<%= js_src_path %>/confirmation/**/*.js','<%= js_src_path %>/home/**/*.js','<%= js_src_path %>/offers/**/*.js','<%= js_src_path %>/productCard/**/*.js','<%= js_src_path %>/settings/**/*.js','<%= js_src_path %>/util/**/*.js' ],
					dest:'<%= js_build_path %>/js/prod-debug<%= curretDate %>.js'
				}
				
			},
			uglify:{
				options:{
					mangle:true
				},
				js:{
					src:'<%= js_build_path %>/<%= js_main_app %>',
					dest:'<%= js_build_path %>/<%= js_main_app %>'
				}
			},
			cssmin: {
			  minify: {
				expand: true,
				cwd: '<%= js_src_path %>/css/',
				src: ['*.css'],
				dest: '<%= js_build_path %>/css/',
				ext: '<%= curretDate %>.min.css'
			  }
			},
			 removelogging: {
				dist: {
				  src: "<%= concat.js.dest %>",
				  dest: "<%= js_build_path %>/<%= js_main_app %>",

				  options: {
					namespace:['console', 'logger']
				  }
				}
			},
			
			
			sass: {
				compile:{
					files: {
						'style.css': 'style.scss'
					}
				}
				},
				
		replace: {
            replaceLocal: {
                src: ['<%= js_build_path %>/index.html'],
                overwrite: true,
                replacements: [{  // replacing app.js with main minified file
                    from: /js\/app.js/g,
                    to: 'js/prod<%= curretDate %>.js'
                },
                {  // replacing ba.css with main minified file
                    from: /js\/templates.js/g,
                    to: 'js/<%= curretDate %>templates.js'
                },
				{  // replacing ba.css with main minified file
                    from: /style.css/g,
                    to: 'style<%= curretDate %>.min.css'
                },
				{  // replacing print.css with main minified file
                    from: /print.css/g,
                    to: 'print<%= curretDate %>.min.css'
                },
				{  // removing script tags, which are pointing to local
                    from: /<script[\s\S]*?>[\s\S]*?<\/script>/g,
                    to: function (matchedWord, index, fullText, regexMatches) {
                        var retunStr = matchedWord;
                        // Keeping External script & main app script  
                        //if(){
                           // retunStr = '' ;
                        //}
						
						if(matchedWord.indexOf("checkout.js") == -1 && matchedWord.indexOf("shake.js") == -1 && matchedWord.indexOf('js/prod') == -1 && matchedWord.indexOf("cordova.js") == -1  && matchedWord.indexOf("lib/") == -1 && matchedWord.indexOf("templates.js") == -1){
                            retunStr = '' ;
                        }
                        return retunStr;
                    }
                }]
            },
			debug: {
                src: ['<%= js_build_path %>/index-debug.html'],
                overwrite: true,
                replacements: [{  // replacing app.js with main minified file
                    from: /js\/prod-debug.js/g,
                    to: 'js/prod-debug<%= curretDate %>.js'
                }]
            }
        },
				
				
				
				
			        copy: {
            main: {
                files: [
                    {expand: true,  cwd: '<%= js_src_path %>',src: "index.html", dest: '<%= js_build_path %>/', filter: 'isFile'},
                    
                    {expand: true,  cwd: '<%= js_src_path %>',src: "shake.js", dest: '<%= js_build_path %>/', filter: 'isFile'},
                    {expand: true,  cwd: '<%= js_src_path %>',src: "lib/ionic/js/ionic.bundle.js", dest: '<%= js_build_path %>/', filter: 'isFile'},
                    {expand: true,  cwd: '<%= js_src_path %>',src: "lib/jquery_m.js", dest: '<%= js_build_path %>/', filter: 'isFile'},
                    {expand: true,  cwd: '<%= js_src_path %>',src: "lib/corouselJquery.js", dest: '<%= js_build_path %>/', filter: 'isFile'},
                    {expand: true,  cwd: '<%= js_src_path %>',src: "lib/lodash.min.js", dest: '<%= js_build_path %>/', filter: 'isFile'},
                    {expand: true,  cwd: '<%= js_src_path %>',src: "lib/ngCordova/dist/ng-cordova.min.js", dest: '<%= js_build_path %>/', filter: 'isFile'},
                    {expand: true,  cwd: '<%= js_src_path %>',src: "lib/numberPicker/ionic-numberpicker.bundle.min.js", dest: '<%= js_build_path %>/', filter: 'isFile'},
                    {expand: true,  cwd: '<%= js_src_path %>',src: "lib/**/*", dest: '<%= js_build_path %>/', filter: 'isFile'},
                    {expand: true,  cwd: '<%= js_src_path %>',src: "img/Easy-Day.jpg", dest: '<%= js_build_path %>/', filter: 'isFile'},
                    {expand: true,  cwd: '<%= js_src_path %>',src: "img/red.gif", dest: '<%= js_build_path %>/', filter: 'isFile'},
                    {expand: true,  cwd: '<%= js_src_path %>',src: "lib/ionic/css/ionic.css", dest: '<%= js_build_path %>/', filter: 'isFile'}
                    
                ]
            }

        },	
				
			ngtemplates: {
				myapp: {
					options: {
						module: "starter",
						htmlmin: {
									
								   removeComments:                 true,
								  collapseWhitespace:             true
						
								  }

		
				},
				 cwd:     '<%= js_src_path %>',
				 //src: "<%= js_src_path %>",
				 src: ["tabs.html","menu.html","add/**/*.html", "admin/**/*.html","address/**/*.html","cart/**/*.html","checkout/**/*.html" ,"confirmation/**/*.html" ,"home/**/*.html","offers/**/*.html","productCard/**/*.html","settings/**/*.html","util/**/*.html"],
				
				dest:'<%= js_build_path %>/js/<%= curretDate %>templates.js'
				}
			}	
				
			}
		);
    grunt.loadNpmTasks('grunt-contrib-clean');
	grunt.loadNpmTasks('grunt-contrib-sass');
	grunt.loadNpmTasks('grunt-angular-templates');
	grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-uglify');
	grunt.loadNpmTasks('grunt-text-replace');
	grunt.loadNpmTasks('grunt-contrib-copy');
	grunt.loadNpmTasks('grunt-remove-logging');
	grunt.loadNpmTasks('grunt-contrib-cssmin');
	
	//grunt.loadNpmTasks('grunt-cachebuster');
	
	
	//grunt.registerTask('default', ['sass','ngtemplates']); 'uglify',
	grunt.registerTask('default', ['clean:cleanBefore','ngtemplates','concat', 'removelogging', 'uglify','cssmin', 'copy','replace:replaceLocal','replace:debug', 'clean:cleanAfter']);
	grunt.registerTask('ci', 'Run the CI build', function () {
	       
        grunt.task.run(['clean:cleanBefore','ngtemplates','concat' , 'removelogging', 'uglify', 'cssmin', 'copy','replace:replaceLocal','replace:debug', 'clean:cleanAfter']);
    });
	  
};
