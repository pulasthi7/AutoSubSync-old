-- ----------------------------------------------------------------------------
-- VLC Extension for AutoSS
--
-- Synchronize the subtitles of a media file with the voice detected from
-- the media file.
--
-- Written by Pulasthi Mahawithana <pulasthi7@gmail.com>
-- ----------------------------------------------------------------------------


function descriptor()
    return { title = "Auto Sub Sync 0.3" ;
             version = "0.3" ;
             author = "Pulasthi Mahawithana" ;
             url = 'http://pulasthi7.blogspot.com';
             shortdesc = "Synchronize subtitles with the Audio";
             description = "This extension synchronizes the subtitles of a media file with the voice detected from the media file" ;
             capabilities = { }
		    }
end

-- Activation
function activate()
	create_main_ui()
end

-- Deactivation
function deactivate()
    
end

-- Main UI Variables
main_ui = nil
btn_start = nil
btn_cancel = nil
btn_restore = nil
btn_about = nil
btn_close = nil
lbl_info = nil
lbl_title = nil
lbl_description = nil
lbl_memory = nil
lbl_range = nil
txt_memory = nil
txt_end_hr = nil
txt_end_min = nil
txt_end_sec = nil
lbl_advanced = nil


--other variables

temp_output = nil
media_path = nil
range_to = nil
memory = nil

function create_main_ui()
	main_ui = vlc.dialog("Auto Sub Sync")
	title_string = "<center style=\"font-size:18px;font-weight:bold;color:white;background-color:black;\">".."Auto Sub Sync".."</center>"
	lbl_title = main_ui:add_label(title_string,1,1,4,4)
	lbl_description = main_ui:add_label("This extension synchronizes the subtitles of a media file with the voice detected from the media file",1,5,4,2)
	lbl_advanced = main_ui:add_label("Advanced Options",1,7,2,1)
	lbl_memory = main_ui:add_label("Memory to use (in MB)",1,8,1,1)
	txt_memory = main_ui:add_text_input("768",2,8,1,1)
	lbl_range = main_ui:add_label("Range until",1,9,1,1)
	txt_end_hr = main_ui:add_text_input("0",2,9,1,1)
	txt_end_min = main_ui:add_text_input("20",3,9,1,1)
	txt_end_sec = main_ui:add_text_input("0",4,9,1,1)
	btn_start = main_ui:add_button("Start",sync_start,1,11,1,1)
	btn_about = main_ui:add_button("About",show_about_ui,3,11,1,1)
	btn_close = main_ui:add_button("Close",close,4,11,1,1)
end

function close()
	main_ui:delete()
	deactivate()
end

function show_about_ui()
	os.execute("xdg-open "..vlc.misc.homedir().."/.autoss/manuals/About.html")	--OS dependent
end


function prepare_ui_for_sync()
	if lbl_description then main_ui:del_widget(lbl_description) end
	if lbl_advanced then main_ui:del_widget(lbl_advanced) end
	if lbl_memory then main_ui:del_widget(lbl_memory) end
	if lbl_range then main_ui:del_widget(lbl_range) end
	if txt_memory then main_ui:del_widget(txt_memory) end
	if txt_end_hr then main_ui:del_widget(txt_end_hr) end
	if txt_end_min then main_ui:del_widget(txt_end_min) end
	if txt_end_sec then main_ui:del_widget(txt_end_sec) end
	if btn_start then main_ui:del_widget(btn_start) end
	lbl_info = main_ui:add_label("",1,5,4,5)
end

function sync_start()
	if tonumber(txt_memory:get_text())>512 then memory = tonumber(txt_memory:get_text()) else memory = 512 end
	range_to = tonumber(txt_end_hr:get_text())*3600 + tonumber(txt_end_min:get_text())*60 + tonumber(txt_end_sec:get_text())
	if not range_to then range_to = 600 end		--set to a default of 10 mins if not set
	if range_to<300 then range_to = 300 end		--lowerbound by 300	to increase accuracy
	if range_to>1800 then range_to = 1800 end	--upperbound by 1800 to increase performance
	prepare_ui_for_sync()
	local item = vlc.input.item()
	if item == nil then return false end
	media_path = item:uri()
	temp_output = vlc.misc.homedir().."/.autoss/tools/temp/out.wav"
	vlc.playlist.pause()
	transcode()
	call_sync()
	vlc.playlist.stop()
end

function call_sync()
	autoss_home = vlc.misc.homedir().."/.autoss"
	--FIXME The subtitle file is assumed to have the same name(and path) of the media file
	sub_file = media_path
	sub_file = sub_file:reverse()
	sub_file = string.gsub(sub_file,"%w*%.","",1)
	sub_file = sub_file:reverse()
	sub_file = sub_file..".srt"
	--FIXME In the following line the temp_output is converted into a URI by adding "file://" at start. Need to fix for a standard way
	process_call = autoss_home.."/init.sh \""..autoss_home.."\" \"file://"..temp_output.."\" \""..sub_file.."\" "..tostring(memory)
	autoss = io.popen(process_call)
	repeat
  		line = autoss:read ("*l") -- read one line
  		if line then  -- if not end of file (EOF)
   			update_info(line) -- print that line
  		end
	until not line
 	autoss:close()
end

function update_info(s)
	lbl_info:set_text(s)
	main_ui:update()
end

function transcode()
	--These calls are non blocking calls and this script doesn't wait them to finish the task
	--FIXME Implement a mechanism to detect and wait till the command is finished.
	vl_manager = vlc.vlm()
	vl_manager:execute_command("new con broadcast enabled")
	vl_manager:execute_command("setup con input '"..media_path.."'")
	vl_manager:execute_command("setup con option stop-time="..range_to)	
	vl_manager:execute_command("setup con output #transcode{vcodec=none,acodec=s16l,ab=16,channels=1,samplerate=16000}:file{dst="..temp_output.."}")
	vl_manager:execute_command("control con play") 
end
