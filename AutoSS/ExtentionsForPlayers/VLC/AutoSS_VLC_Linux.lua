-- ----------------------------------------------------------------------------
-- VLC Extension for AutoSS
--
-- Synchronize the subtitles of a media file with the voice detected from
-- the media file.
--
-- Written by Pulasthi Mahawithana <pulasthi7@gmail.com>
-- ----------------------------------------------------------------------------


function descriptor()
    return { title = "Auto Sub Sync 0.1" ;
             version = "0.1" ;
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

-- Variables
main_ui = nil
btn_start = nil
btn_cancel = nil
try_from = nil
try_till = nil
media_path = nil
lbl_info = nil
temp_output = nil

function create_main_ui()
	main_ui = vlc.dialog("Auto Sub Sync")	--the dialog box
	title_string = "<center style=\"font-size:32px;font-weight:bold;color:white;background-color:black;\">".."Auto Sub Sync".."</center>"
	main_ui:add_label(title_string,1,1,2,1)
	btn_start = main_ui:add_button("Start",sync_start,1,2,1,1)
end

function sync_start()
	if btn_start then
		main_ui:del_widget(btn_start)
	end
	local item = vlc.input.item()
	if item == nil then return false end
	media_path = item:uri()
	--TODO Following should be changed
	temp_output = vlc.misc.homedir().."/.autoss/tools/temp/out.wav"
	vlc.playlist.pause()
	update_info("Transcoding...")
	transcode()
	update_info(media_path.."\n"..temp_output)
	call_sync()
	vlc.playlist.stop()
	vlc.playlist.play()
end

function call_sync()
	autoss_home = vlc.misc.homedir().."/.autoss"
	--FIXME The subtitle file is assumed to have the same name(and path) of the media file
	--FIXME The name of the media file is assumed to have no '.'s other than the one before the extension 
	sub_file = string.gsub(media_path, "%.%w*", ".srt")
	--FIXME Allocate dynamic memory size
	max_memory = "512"
	--FIXME conversion from URI to path is done by taking the substring from 8 to end. use a proper method instead
	process_call = autoss_home.."/init.sh "..autoss_home.." "..temp_output.." "..string.sub (sub_file, 8).." "..max_memory
	update_info(process_call)
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
	if not lbl_info then
		lbl_info = main_ui:add_label(s,1,2,1,1)
	else
		lbl_info:set_text(s)
	end
end

function transcode()
	--These calls are non blocking calls and this script doesn't wait them to finish the task
	--FIXME Implement a mechanism to detect and wait till the command is finished.
	vl_manager = vlc.vlm()
	vl_manager:execute_command("new con broadcast enabled")
	vl_manager:execute_command("setup con input '"..media_path.."'")
	vl_manager:execute_command("setup con output #transcode{vcodec=none,acodec=s16l,ab=16,channels=1,samplerate=16000}:file{dst="..temp_output.."}")
	vl_manager:execute_command("control con play") 
end

